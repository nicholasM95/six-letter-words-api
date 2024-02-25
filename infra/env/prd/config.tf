terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.38.0"
    }

    helm = {
      source  = "hashicorp/helm"
      version = "2.9.0"
    }

    cloudflare = {
      source  = "cloudflare/cloudflare"
      version = "4.25.0"
    }

  }

  backend "s3" {
    bucket = "nicholasmeyers-six-letter-words-api-prd-terraform-state"
    key    = "application.tfstate"
    region = "eu-west-1"
  }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      "app" = "nicholasmeyers_six_letter_words_api"
    }
  }
}

provider "helm" {
  kubernetes {
    config_path = "/home/runner/work/six-letter-words-api/six-letter-words-api/k8s.config"
  }
}

provider "cloudflare" {}
