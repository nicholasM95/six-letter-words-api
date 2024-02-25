resource "random_password" "password" {
  length           = 16
  special          = true
  override_special = "!#$%&*()-_=+[]{}<>:?"
}

resource "aws_db_instance" "database" {
  allocated_storage                   = 5
  iam_database_authentication_enabled = true
  identifier                          = var.database_name
  db_name                             = var.database_name
  engine                              = "postgres"
  engine_version                      = "14.7"
  instance_class                      = "db.t3.micro"
  username                            = "master"
  password                            = random_password.password.result
  publicly_accessible                 = true
  skip_final_snapshot                 = true
  db_subnet_group_name                = data.aws_ssm_parameter.db_subnet_group_name.value
  vpc_security_group_ids              = [aws_security_group.security_group.id]
}

resource "postgresql_role" "db_user" {
  name       = var.database_username
  login      = true
  roles      = ["rds_iam"]
  depends_on = [aws_db_instance.database]
  lifecycle {
    prevent_destroy = true
  }
}
