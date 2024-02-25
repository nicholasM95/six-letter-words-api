module "public_database" {
  source              = "../../modules/public_database"
  database_name       = "sixletterwordsapi"
  database_username   = "sixletterwordsapi"
  security_group_name = "six-letter-words-api"
}

module "s3_bucket" {
  source         = "../../modules/s3_bucket"
  s3_bucket_name = "six-letter-words-api"
}

module "dns" {
  source    = "../../modules/dns"
  host_name = "nicholasmeyers.be"
  ip        = var.public_ip
  name      = "six-letter-words-api"
}

module "application" {
  depends_on           = [module.dns]
  source               = "../../modules/application"
  account              = "896918338968"
  database_resource_id = module.public_database.database_resource_id
  database_url         = module.public_database.database_url
  database_user        = "sixletterwordsapi"
  helm_path            = "../../../helm/application"
  image_tag            = var.image_tag
  name                 = "six-letter-words-api"
  namespace            = "six-letter-words-api"
  region               = "eu-west-1"
  bucket_arn           = module.s3_bucket.s3_bucket_arn
  bucket_name          = "six-letter-words-api"
}
