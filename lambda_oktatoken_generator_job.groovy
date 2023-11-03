def jobs = [
    [job_name:'cp-aws-lambda-oktatoken-generator', gitURL: 'git@bitbucket.org:copperpoint/cp-aws-lambda-oktatoken-generator.git', FunctionName: 'cp-aws-lambda-oktatoken-generator']
]

for (item in jobs){
    lambdaDeploymentJobs(item.get('job_name'), item.get('gitURL'), item.get('FunctionName'))
}
def lambdaDeploymentJobs(job_name, gitURL, FunctionName){
    pipelineJob("${job_name}"){
        logRotator{
            numToKeep(5)
        }

        description("Job for deploying ${job_name} to AWS")
        parameters{
            activeChoiceParam('environment'){
                description('Select the environment')
                choiceType('RADIO')
                groovyScript{
                    script('return ["dev", "prod"]')
                }
            }
            activeChoiceReactiveParam('SecurityGroup') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('dev')){return['sg-0016836cb8aacbc23:selected']} else if (environment.equals('prod')){return['sg-0ab365b1bd5ba6947:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('SubnetA') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('dev')){return['subnet-047c3c97ab26ed263:selected']} else if (environment.equals('prod')){return['subnet-0d4d44c8f81a888d4:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('SubnetB') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('dev')){return['subnet-0684240fab6c949ae:selected']} else if (environment.equals('prod')){return['subnet-0178d984fb3c6ea23:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('SubnetC') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('dev')){return['subnet-064511c099829fe3d:selected']} else if (environment.equals('prod')){return['subnet-009a020f928b1f8a6:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('gitBranch') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('dev')){return['developer:selected']} else if (environment.equals('prod')){return['main:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('s3_bucket') {
                description('Name of S3 Bucket for Artifact Packaging')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('dev')){return['${job_name}-dev:selected']} else if (environment.equals('prod')){return['${job_name}-prod:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('stack_name') {
                description('Name of Cloudformation Stack')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('dev')){return['${job_name}-dev:selected']} else if (environment.equals('prod')){return['${job_name}-prod:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('role') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('dev')){return['arn:aws:iam::594834477086:role/cpic-sandbox-integ:selected']} else if (environment.equals('prod')){return['arn:aws:iam::448503678883:role/cpic-integ-prod:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('FunctionName') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('dev')){return['${job_name}-dev:selected']} else if (environment.equals('prod')){return['${job_name}-prod:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('Lambda_Env') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('dev')){return['NonProd:selected']} else if (environment.equals('prod')){return['Prod:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('cred_id') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('dev')){return['AWS service account:selected']} else if (environment.equals('prod')){return['aws-service-account-prod:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceParam('token_scope'){
                description('Scope of Token Access')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script('return ["offline_access"]')
                }
                
            }
            activeChoiceParam('token_grant_type'){
                description('Grant Type of Token Access')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script('return ["password"]')
                }
                
            }
            activeChoiceParam('token_secret_name'){
                description('Secret Name for Token')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script('return ["cpic-nonprod-okta-token-VcWHOM"]')
                }
                
            }
            activeChoiceParam('token_region'){
                description('Region of Token')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script('return ["us-west-2"]')
                }
                
            }
            activeChoiceParam('token_url'){
                description('URL for Token Retrieval')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('dev')){return['https://dev-04923793.okta.com/oauth2/default/v1/token']} else if (environment.equals('prod')){return['https://copperpoint.okta.com/oauth2/default/v1/token']}")
                }
                referencedParameter('environment')
            }
            // wReadonlyStringParameterDefinition {
            //     name('gitURL')
            //     defaultValue("$gitURL")
            //     description('')
            // }
            choiceParam('MemorySize', ['1024', '2048', '3072'], '')
            choiceParam('Timeout', ['180', '600', '900'], '')
            choiceParam('Architecture', ['x86_64'], '')
            choiceParam('Lambda_Runtime', ['python3.11'], '')
            choiceParam('Log_Collection', ['True', 'False'], '')
            stringParam('lambda_layer', 'Sample Lambda Layer', '')
            definition {
                cps{
                    script(readFileFromWorkspace('./lambda_oktatoken_generator_pipeline.groovy'))
                    sandbox()
                }
            }
        }
    }
}
