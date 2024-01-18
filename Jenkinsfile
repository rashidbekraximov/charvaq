pipeline {
    agent any
    tools{
        maven 'maven_3_9_2'
    }
    stages{
        stage('clone from github'){
            steps{
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/rashidbekraximov/charvaq']])
                sh 'mvn clean install'
            }
        }
        stage('Build docker image'){
            steps{
                script{
                    sh 'docker build -t charvaq .'
                }
            }
        }
        stage('Push to Docker Hub'){
            steps{
                script{
                    sh 'docker login -u rashidbek -p hashcode8864'
                    sh 'docker tag charvaq rashidbek/charvaq:latest'
                    sh 'docker push rashidbek/charvaq:latest'
                }
            }
        }
    }

}
