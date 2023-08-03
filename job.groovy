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
  definition {
    cps {
        pipeline {
            agent any
            stages {
                stage("pull") {
            
                    steps {
                    shell '''
                    cd aws_demo
                    git(${env.GITREPO}, ${env.GITBRANCH})
                    '''
                    }
                 }
        stage("build") {
            steps {
            shell '''
            cd aws_demo
            cd hello_world
            compress-archive -DestinationPath ${env.DESINATION_PATH}  -Path ${PATH}
            aws lambda update-function-code --function-name ${env.FUNCTION_NAME} --zip-file ${env.ZIP_FILE} --force
            '''
            }
        }
        
    }
}
    }
  }
}