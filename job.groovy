job('demoLambda-Seed-Job') {
	description("Seed job with parameters for demolambda")
  environmentVariables {
        
    }
  scm {
    git(GIT_REPO,GIT_BRANCH)
  }
  steps {
    samDeploy(settings())
  }
}