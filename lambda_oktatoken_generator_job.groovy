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
                    script('return ["nonprd", "prod"]')
                }
            }
            activeChoiceReactiveParam('SecurityGroup') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('nonprd')){return['sg-0e03e244b6239de0d:selected']} else if (environment.equals('prod')){return['sg-0368890663d3ef3a4:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('SubnetA') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('nonprd')){return['subnet-08840e286a6ef0a44:selected']} else if (environment.equals('prod')){return['subnet-0d4d44c8f81a888d4:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('SubnetB') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('nonprd')){return['subnet-0af7e31a81b8d8810:selected']} else if (environment.equals('prod')){return['subnet-0178d984fb3c6ea23:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('SubnetC') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('nonprd')){return['subnet-0911ce3c16fa99d77:selected']} else if (environment.equals('prod')){return['subnet-009a020f928b1f8a6:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('gitBranch') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('nonprd')){return['developer:selected']} else if (environment.equals('prod')){return['main:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('s3_bucket') {
                description('Name of S3 Bucket for Artifact Packaging')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('nonprd')){return['${job_name}-nonprd:selected']} else if (environment.equals('prod')){return['${job_name}-prod:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('stack_name') {
                description('Name of Cloudformation Stack')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('nonprd')){return['${job_name}-nonprd:selected']} else if (environment.equals('prod')){return['${job_name}-prod:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('role') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('nonprd')){return['arn:aws:iam::314704840185:role/cpic-integ-nonprd:selected']} else if (environment.equals('prod')){return['arn:aws:iam::448503678883:role/cpic-integ-prod:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('FunctionName') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('nonprd')){return['${job_name}-nonprd:selected']} else if (environment.equals('prod')){return['${job_name}-prod:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('Lambda_Env') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('nonprd')){return['NonProd:selected']} else if (environment.equals('prod')){return['Prod:selected']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            activeChoiceReactiveParam('cred_id') {
                description('')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('nonprd')){return['AWS service account:selected']} else if (environment.equals('prod')){return['aws-service-account-prod:selected']}")
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
                    script('return ["cpic-nonprod-okta-token"]')
                }
                
            }
            activeChoiceParam('token_region'){
                description('Region of Token')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script('return ["us-west-2"]')
                }
                
            }
            activeChoiceReactiveParam('token_url'){
                description('URL for Token Retrieval')
                choiceType('SINGLE_SELECT')
                groovyScript {
                    script("if (environment.equals('nonprd')){return['https://dev-04923793.okta.com/oauth2/default/v1/token']} else if (environment.equals('prod')){return['https://copperpoint.okta.com/oauth2/default/v1/token']}")
                    fallbackScript('return["error"]')
                }
                referencedParameter('environment')
            }
            wReadonlyStringParameterDefinition {
                name('gitURL')
                defaultValue("$gitURL")
                description('')
            }
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
