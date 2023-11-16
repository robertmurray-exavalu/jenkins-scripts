dev_env = params.environment in ['dev']
prod_env = params.environment in ['prod']

approvers_dev_env = ['sray@copperpoint.com']
approvers_prod_env = ['sray@copperpoint.com']

pipeline {
    agent {
        label 'ecs'
    }

    stages {
        stage('Checkout') {
            steps{
                deleteDir()
                approval(params.environment)
                git branch: "main",
                credentialsId: 'c109308d-28d6-4bed-bd08-60f713447612',
                url: "git@github.com:robertmurray-exavalu/testJenkinsDeploy.git"
            }
        }
        stage('Python Test') {
            steps{
                bat '''
                python3.11 -m pip install -r requirements.txt 
                '''
                bat '''
                python3.11 -m pytest --cov --cov-report=html --cov-fail-under=80
                '''
            }
            post{
                always{
                    powershell'''
                        git clean -d -f -x &&
                        rmdir -r tests &&
                        rm requirements.txt &&
                        ls &&
                        rm .gitignore &&
                        ls 
                    '''
                }
            }
            
        }
        
    }
}
def approval(environment) {
script{
wrap([$class: 'BuildUser']) {
        user = env.BUILD_USER_ID
     
    def approvers
    def user_approver
    if (dev_env){
        approvers = approvers_dev_env
        println "Please approve the deployment of the lambda function to ${environment}"
    }else if (prod_env){
        approvers = approvers_prod_env
        println "Please approve the deployment of the lambda function to ${environment}"
    }
        echo "approval list: ${approvers}"
        for (item in approvers){
        emailext (
            subject: "Waiting for your approval to deploy to ${environment} environment! Job: '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
            body: "Pipeline started by: ${user}<br><a href=${BUILD_URL}input>Click here  to approve/abort</a>",
            to: "${item}", mimeType: 'text/html'
        )
        }
        timeout(time: 60, unit: 'MINUTES'){
         userInput = input id: 'userInput',
                              message: 'Proceed for Deployment',
                              submitterParameter: 'submitter',
                              submitter: 'Approval',
                              parameters: [[$class: 'BooleanParameterDefinition', defaultValue: true,, description:'', name:'Approved']]
        }
        user_approver = userInput['submitter']
        if("${approvers}".contains("${user_approver}") && ("${user_approver}"!="${user}")){
             echo 'Approved'
        }else{
             //error_message('You cannot approve your own pipeline or you should be part of approval group')
        }
}
}
}

def failure(stage){
script{
wrap([$class: 'BuildUser']) {
        user = env.BUILD_USER_ID
     emailext attachLog: true, body: "Pipeline started by: ${user}<br> Failed in ${stage} Stage ${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} <br> More info at: ${env.BUILD_URL}", subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}:${env.BUILD_NUMBER}", to: "$notification_list", mimeType: 'text/html'
}
}
}

def error_message(message)
{
    currentBuild.result = 'ABORTED'
    error "${message}, Build is aborted"
}
