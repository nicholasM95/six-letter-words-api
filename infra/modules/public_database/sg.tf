resource "aws_security_group" "security_group" {
  name        = var.security_group_name
  description = "Allow inbound traffic for PG"
  vpc_id      = data.aws_ssm_parameter.vpc_id.value

  ingress {
    description      = "PG connection"
    from_port        = 5432
    to_port          = 5432
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }
}
