job('demoLambda-Seed-Job') {
	description("Seed job with parameters for demolambda")
  parameters{
    parameters{
        stringParam(name: 'GITREPO', defaultValue: 'https://github.com/robertmurray-exavalu/jenkins-scripts.git')
        string(name: 'GITBRANCH', defaultValue: 'main')
        string(name: 'DESTINATION_PATH', defaultValue: 'vsCodeCSVLambda.zip')
        string(name: 'PATH', defaultValue: 'lambda_function.py')
        string(name: 'FUNCTION_NAME', defaultValue: 'vsCodeCSVLambda')
        string(name: 'ZIP_FILE', defaultValue: 'fileb://vsCodeCSVLambda.zip')
        credentials(name: 'Lambda Access', defaultValue: 'AKIASJUDC4AAITRUG4SE (Lambda Access)')
    }
  }
  scm {
    git(GIT_REPO,GIT_BRANCH)
  }
  steps {
    script('pipeline.groovy')
  }
}