pipeline {
    agent any

    tools {
        maven 'Maven-3'
        jdk   'JDK-21'
    }

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['QA', 'DEV', 'STAGING'],
            description: 'Environment to run tests against'
        )
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/omontielc/automation-framework-back.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile -DskipTests'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test -DsuiteXmlFile=testng.xml'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Allure Report') {
            steps {
                allure([
                    includeProperties: false,
                    jdk: 'JDK-21',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }

    post {
        always {
            echo "Pipeline finished — Environment: ${params.ENVIRONMENT}"
        }
        success {
            echo 'All API tests passed'
        }
        failure {
            echo 'API tests failed — check Allure report'
        }
    }
}