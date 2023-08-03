job('demoLambda-Seed-Job') {
	description("Seed job with parameters for demolambda")
  parameters{
    parameters{
        stringParam('GITREPO', 'https://github.com/robertmurray-exavalu/jenkins-scripts.git')
        stringParam('GITBRANCH', 'main')
        stringParam('DESTINATION_PATH', 'vsCodeCSVLambda.zip')
        stringParam('PATH', 'lambda_function.py')
        stringParam('FUNCTION_NAME', 'vsCodeCSVLambda')
        stringParam('ZIP_FILE', 'fileb://vsCodeCSVLambda.zip')
        credentialsParam('Lambda Access') {
            type('com.cloudbees.jenkins.plugins.awscredentials')
            required()
            defaultValue('AKIASJUDC4AAITRUG4SE (Lambda Access)')
        }
    }
  }
  scm {
    git(GIT_REPO,GIT_BRANCH)
  }
  cps {
    script(readFileFromWorkspace('pipeline.groovy'))
    sandbox()
  }
}