output "database_url" {
  value = "jdbc:postgresql://${aws_db_instance.database.endpoint}/${aws_db_instance.database.db_name}?ssl=true&sslmode=require"
}

output "database_resource_id" {
  value = aws_db_instance.database.resource_id
}
