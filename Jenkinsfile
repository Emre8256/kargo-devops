pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Verify') {
            steps {
                bat 'if not exist target\\kargo-0.0.1-SNAPSHOT.jar exit /b 1'
            }
        }

        stage('Deploy') {
            steps {
                bat 'docker compose up --build -d'
            }
        }
    }

    post {
        success {
            mail to: '42oyunus42@gmail.com',
                 subject: 'Kargo Pipeline Başarılı',
                 body: 'Kargo projesi başarıyla build edildi ve deploy edildi.'
        }

        failure {
            mail to: '42oyunus42@gmail.com',
                 subject: 'Kargo Pipeline Başarısız',
                 body: 'Pipeline sırasında bir hata oluştu.'
        }
    }
}