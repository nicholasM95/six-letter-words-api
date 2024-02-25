data "aws_ssm_parameter" "vpc_id" {
  name = "/infra/public/vpc"
}

data "aws_ssm_parameter" "db_subnet_group_name" {
  name = "/infra/public/db-subnet-group-name"
}
