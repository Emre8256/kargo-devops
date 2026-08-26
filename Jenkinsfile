pipeline {
    agent {
        label 'java-agent'
    }

    stages {
        stage('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }
    }
}