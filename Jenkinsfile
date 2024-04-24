pipeline {
    agent any

    environment {
        A = 5000
        B = 1000
    }

    options {
        timeout(time:5, unit: 'MINUTES')
    }

    stages {
        stage("Operaciones aritméticas") {
            steps {
                script {
                    a = env.A.toInteger()
                    b = env.B.toInteger()
                    
                    println "a = " + a
                    println "b = " + b
                   
                    println "La suma es igual a: " + (a + b)
                    println "La resta es igual a: " + (a - b)
                    println "La multiplicación es igual a: " + (a * b)

                    if (b != 0) {
                        println 'La división de ${a} y ${b} es igual a: ' + (a / b)
                    } else {
                        println 'No se puede dividir por cero'
                    }
                }
            }
        }
        stage("Comando ipconfig /flushdns") {
            steps
            {
                bat "ipconfig /flushdns"
            }
        }
        stage("Generación de fichero .txt") {
            steps {
                script {
                    def errorMessage = currentBuild.rawBuild.getLog(1000).join('\n')
                    writeFile file: 'info_stage_1.txt', text: "${errorMessage}"
                    println 'Archivo TXT generado con la información del Stage 1.'
                }
            }
        }
    }

    post {
        always {
            echo "El pipeline se ejecuto. "
        }
        failure {
            echo 'EL pipeline fallo'
            script {                
                mail to: "soporte@dominio.com", subject: "Pipeline Fallo", body: "El pipeline no se ejecuto."
            }
        }
        unstable {
            echo "El pipeline no se ejecuto de forma estable."
        }
        success {
            echo "Mensaje a ejecutar si salio todo bien"        
        }
    }
}
