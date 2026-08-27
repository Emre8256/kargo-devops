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

    post {
    success {
        mail to: '42oyunus42@gmail.com',
             subject: 'Kargo Pipeline Başarılı',
             body: 'Kargo projesi başarıyla build edildi.'
    }

    failure {
        mail to: '42oyunus42@gmail.com',
             subject: 'Kargo Pipeline Başarısız',
             body: 'Pipeline sırasında bir hata oluştu.'
    }
}
}