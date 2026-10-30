pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'satyamdevang/calculator'
        IMAGE_TAG    = "${env.BUILD_NUMBER}"
        NOTIFY_EMAIL = 'satyam.dewangan@iiitb.ac.in'
        APP_URL      = 'http://localhost:8081'   // Nginx in front of the replicas
    }

    triggers {
        githubPush()                  // fired by the GitHub webhook (Step 8)
        // pollSCM('H/2 * * * *')     // fallback if the webhook can't reach Jenkins
    }

    options {
        disableConcurrentBuilds()     // two deploys never race each other
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE:$IMAGE_TAG -t $DOCKER_IMAGE:latest .'
            }
        }

        stage('Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds',
                                                  usernameVariable: 'DH_USER',
                                                  passwordVariable: 'DH_TOKEN')]) {
                    sh '''
                        echo "$DH_TOKEN" | docker login -u "$DH_USER" --password-stdin
                        docker push $DOCKER_IMAGE:$IMAGE_TAG
                        docker push $DOCKER_IMAGE:latest
                    '''
                }
            }
            post {
                always {
                    sh 'docker logout || true'
                }
            }
        }

        stage('Deploy') {
            steps {
                // DOCKER_IMAGE and IMAGE_TAG come from the environment block; compose reads them
                sh 'docker compose up -d --remove-orphans --wait --wait-timeout 180'
                sh '''
                    for i in $(seq 1 20); do
                        if curl -fs $APP_URL/actuator/health | grep -q '"status":"UP"'; then
                            echo "Deployment healthy"
                            curl -fs "$APP_URL/api/v1/add?a=2&b=3"
                            exit 0
                        fi
                        sleep 3
                    done
                    echo "Health check failed"
                    docker compose ps
                    docker compose logs --tail=50
                    exit 1
                '''
            }
        }
    }

    post {
        success {
            emailext to: "${env.NOTIFY_EMAIL}",
                     subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                     body: "Pipeline succeeded.\nDeployed image: ${env.DOCKER_IMAGE}:${env.IMAGE_TAG}\nApp: ${env.APP_URL}\nDetails: ${env.BUILD_URL}"
        }
        failure {
            emailext to: "${env.NOTIFY_EMAIL}",
                     subject: "FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                     body: "Pipeline failed.\nConsole log: ${env.BUILD_URL}console"
        }
        always {
            sh 'docker image prune -f || true'
        }
    }
}
