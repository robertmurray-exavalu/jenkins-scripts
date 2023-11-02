dev_env = params.environment in ['dev']
prod_env = params.environment in ['prod']

approvers_dev_env = ['sray@copperpoint.com','CP0365-esb-cp-itsupport@copperpoint.com', 'CPO365-ITArchitecture@copperpoint.onmicrosoft.com']

pipeline {
    agent {
        label 'ecs'
    }

    stages {
        stage('Checkout') {
            steps{
                deleteDir()
                approval(params.environment)
                git branch: "${gitBranch}",
                credentialsId: 'ec01eb6d-13c9-432a-b288-c449ad7b7d61',
                url: "${gitURL}"
            }
            post {
                failure {
                    failure('Checkout')
                }
            }
        }
        stage('Python Test') {
            steps{
                sh '''
                python3 -m pytest --cov --cov-report=html --cov-fail-under=80
                '''
            }
            post {
                always{
                    publishHTML (target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: 'htmlcov',
                    reportFiles: '*.html',
                    reportName: "Pytest-Report"
                    ])
                }
                failure {
                    failure('Python Test')
                }
            }
        }
        stage('Build & Deploy'){
            steps{
                samDeploy([credentialsId: "$cred_id", kmsKeyId: '', outputTemplateFile: 'template-packaged.yaml', parameters: [[key: 'SecurityGroup', value: "$SecurityGroup"], [key: 'SubnetA', value: "$SubnetA"], [key: 'SubnetB', value: "$SubnetB"], [key: 'SubnetC', value: "$SubnetC"], [key: 'FunctionName', value: "$FunctionName"], [key: 'MemorySize', value: "$MemorySize"], [key: 'Timeout', value: "$Timeout"], [key: 'Role', value: "$role"], [key: 'Architecture', value: "$Architecture"], [key: 'Runtime', value: "$Lambda_Runtime"], [key: 'LambdaLayer', value: "$lambda_layer"], [key: 'LogCollection', value: "$Log_Collection"], [key: 'LambdaEnv', value: "$Lambda_Env"],[key: 'scope', value: "$token_scope"],[key: 'grant_type', value: "$token_grant_type"],[key: 'secret_name', value: "$token_secret_name"],[key: 'region_name', value: "$token_region"], [key: 'url', value: "$token_url"]], region: 'us-west-2', roleArn: '', s3Bucket: "$s3_bucket", s3Prefix: '', stackName: "$stack_name", templateFile: 'template.yaml'])
            }
        }
    }
}