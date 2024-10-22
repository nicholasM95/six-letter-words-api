terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.38.0"
    }

    postgresql = {
      source  = "cyrilgdn/postgresql"
      version = "1.24.0"
    }
  }
}

provider "postgresql" {
  host            = aws_db_instance.database.address
  port            = aws_db_instance.database.port
  database        = "postgres"
  username        = aws_db_instance.database.username
  password        = aws_db_instance.database.password
  sslmode         = "require"
  connect_timeout = 15
  superuser       = false
}
