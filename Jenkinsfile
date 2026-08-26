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
                sh 'test -f target/kargo-0.0.1-SNAPSHOT.jar'
            }
        }
    }
}