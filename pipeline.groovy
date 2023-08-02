pipelineJob('pipeline-demo') {
    definition {
        cps {
            script('''
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
            '''.stripIndent())
            sandbox()
        }
    }
}