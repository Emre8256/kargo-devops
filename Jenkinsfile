pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Verify') {
            steps {
                sh 'ls -la target'
            }
        }
    }
}