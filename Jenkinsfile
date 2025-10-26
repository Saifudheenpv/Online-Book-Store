pipeline {
    agent any

    environment {
        // SonarQube
        SONARQUBE = 'Sonar-Server'
        SCANNER = 'Sonar-Scanner'

        // Docker
        DOCKER_CREDENTIALS = credentials('dockerhub-creds')
        DOCKER_IMAGE = "saifudheenpv/online-book-store"

        // Jenkins Tools
        MAVEN_HOME = tool 'Maven3'
        JAVA_HOME = tool 'JDK17'
        PATH = "$MAVEN_HOME/bin:$JAVA_HOME/bin:$PATH"

        // Email
        SMTP_CREDENTIALS = credentials('gmail-smtp')
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Saifudheenpv/Online-Book-Store.git'
            }
        }

        stage('Maven Compile & Test') {
            steps {
                sh 'mvn clean compile test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('Sonar-Server') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=Online-Book-Store -Dsonar.projectName="Online Book Store"'
                }
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build & Push') {
            steps {
                sh """
                docker build -t $DOCKER_IMAGE:latest .
                echo $DOCKER_CREDENTIALS_PSW | docker login -u $DOCKER_CREDENTIALS_USR --password-stdin
                docker push $DOCKER_IMAGE:latest
                """
            }
        }

        stage('Trivy Scan') {
            steps {
                sh 'trivy image $DOCKER_IMAGE:latest || true'
            }
        }

        stage('Deploy Container') {
            steps {
                sh """
                docker stop onlinebookstore || true
                docker rm onlinebookstore || true
                docker run -d --name onlinebookstore -p 8081:8080 $DOCKER_IMAGE:latest
                """
            }
        }
    }

    post {
        success {
            emailext (
                subject: "✅ SUCCESS: Jenkins Build #${env.BUILD_NUMBER} - ${env.JOB_NAME}",
                body: "Build #${env.BUILD_NUMBER} succeeded. Check: ${env.BUILD_URL}",
                to: 'mesaifudheenpv@gmail.com',
                from: SMTP_CREDENTIALS_USR
            )
        }
        failure {
            emailext (
                subject: "❌ FAILURE: Jenkins Build #${env.BUILD_NUMBER} - ${env.JOB_NAME}",
                body: "Build #${env.BUILD_NUMBER} failed. Check: ${env.BUILD_URL}",
                to: 'mesaifudheenpv@gmail.com',
                from: SMTP_CREDENTIALS_USR
            )
        }
    }
}
