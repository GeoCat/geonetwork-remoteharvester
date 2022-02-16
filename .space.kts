/**
* JetBrains Space Automation
* This Kotlin-script file lets you automate build activities
* For more info, see https://www.jetbrains.com/help/space/automation.html
*/

job("Build, test and install project artifacts") {
    
    container(displayName = "Run mvn install", image = "maven:3-jdk-8") {
        // url of a Space Packages repository
        env["REPOSITORY_URL"] = "https://maven.pkg.jetbrains.space/geocat/p/jrc-inspire-portal/maven"


        shellScript {
            content = """
            	set -e
            	echo === Build artifacts... ===
	            mvn -B clean install -DskipTests
                echo === Run the tests... ===
                mvn -B test
                echo === Publish artifacts... ===
                mvn -B deploy -s .space/settings.xml \
                    -DskipTests \
                    -DrepositoryUrl=${'$'}REPOSITORY_URL \
                    -DspaceUsername=${'$'}JB_SPACE_CLIENT_ID \
                    -DspacePassword=${'$'}JB_SPACE_CLIENT_SECRET
            """
        }
    }

    docker {
         beforeBuildScript {
            // Create an env variable BRANCH,
            // use env var to get full branch name,
            // leave only the branch name without the 'refs/heads/' path
            content = """
                export BRANCH=${'$'}(echo ${'$'}JB_SPACE_GIT_BRANCH | cut -d'/' -f 3)
            """
        }
        build {
            context = "."
            file = "./Dockerfile"
            labels["vendor"] = "GeoCat B.V."
        }

        push("geocat.registry.jetbrains.space/p/jrc-inspire-portal/docker/csw-ingester") {
            // Use the BRANCH and JB_SPACE_EXECUTION_NUMBER env vars
            tags("\$BRANCH", "\$BRANCH-\$JB_SPACE_EXECUTION_NUMBER")
        }
    }
    

    container(
        displayName="Push Docker image in GeoCat Docker repository", 
        image="geocat.registry.jetbrains.space/p/sys-maint/docker/crane:main"
    ) {
        env["GEOCAT_DOCKER_REGISTRY_URL"] = "docker-registry.geocat.net:5000"
        env["GEOCAT_DOCKER_REGISTRY_USER"] = Params("geocat_docker_registry_user")
        env["GEOCAT_DOCKER_REGISTRY_PASSWORD"] = Secrets("geocat_docker_registry_password")
        env["GITHUB_REGISTRY_USERNAME"] = Params("github_registry_username")
        env["GITHUB_REGISTRY_PAT"] = Secrets("github_registry_pat")
        
        shellScript {
            content = """
            	BRANCH=${'$'}(echo ${'$'}JB_SPACE_GIT_BRANCH | cut -d'/' -f 3)
                crane auth login geocat.registry.jetbrains.space -u ${'$'}JB_SPACE_CLIENT_ID -p ${'$'}JB_SPACE_CLIENT_SECRET
                crane auth login ${'$'}GEOCAT_DOCKER_REGISTRY_URL -u ${'$'}GEOCAT_DOCKER_REGISTRY_USER -p ${'$'}GEOCAT_DOCKER_REGISTRY_PASSWORD
                crane auth login ghcr.io -u ${'$'}GITHUB_REGISTRY_USERNAME -p ${'$'}GITHUB_REGISTRY_PAT

                
				crane copy geocat.registry.jetbrains.space/p/jrc-inspire-portal/docker/csw-ingester:${'$'}BRANCH-${'$'}JB_SPACE_EXECUTION_NUMBER ${'$'}GEOCAT_DOCKER_REGISTRY_URL/jrc-inspire-portal/csw-ingester:${'$'}BRANCH-${'$'}JB_SPACE_EXECUTION_NUMBER
				crane copy geocat.registry.jetbrains.space/p/jrc-inspire-portal/docker/csw-ingester:${'$'}BRANCH-${'$'}JB_SPACE_EXECUTION_NUMBER ${'$'}GEOCAT_DOCKER_REGISTRY_URL/jrc-inspire-portal/csw-ingester:${'$'}BRANCH

                crane copy geocat.registry.jetbrains.space/p/jrc-inspire-portal/docker/csw-ingester:${'$'}BRANCH-${'$'}JB_SPACE_EXECUTION_NUMBER ghcr.io/geocat/csw-ingester:${'$'}BRANCH-${'$'}JB_SPACE_EXECUTION_NUMBER
				crane copy geocat.registry.jetbrains.space/p/jrc-inspire-portal/docker/csw-ingester:${'$'}BRANCH-${'$'}JB_SPACE_EXECUTION_NUMBER ghcr.io/geocat/csw-ingester:${'$'}BRANCH
			"""
        }
    }

    

}
