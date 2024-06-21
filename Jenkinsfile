pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/rashidbekraximov/charvaq.git'
        DOCKER_IMAGE = 'rashidbek/charvaq:v1.0.2'
        GIT_CREDENTIALS_ID = 'github-creds'
        DOCKER_CREDENTIALS_ID = 'dockerhub-creds'
        CONTAINER_NAME = 'charvaq'
    }

    stages {

        stage('Stop and Remove Container') {
            steps {
                script {
                    echo "Stopping Docker container ${CONTAINER_NAME}"
                    sh "docker stop ${CONTAINER_NAME} || true"
                    echo "Removing Docker container ${CONTAINER_NAME}"
                    sh "docker rm ${CONTAINER_NAME} || true"
                }
            }
        }

        stage('Checkout') {
            steps {
                git url: "${env.GIT_REPO}", credentialsId: "${env.GIT_CREDENTIALS_ID}"
            }
        }

        stage('Build') {
            steps {
                script {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${env.DOCKER_IMAGE}")
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    sh "docker run -d -p 3030:3030 --name charvaq ${env.DOCKER_IMAGE}"
                }
            }
        }
    }

    post {
        always {
            script {
                echo "Cleaning up"
                sh "docker stop 3030:3030 || true"
                sh "docker rm 3030:3030 || true"
            }
        }
    }
}
