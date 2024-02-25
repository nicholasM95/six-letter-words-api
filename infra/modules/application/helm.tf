resource "helm_release" "helm_release" {
  name             = var.name
  chart            = var.helm_path
  namespace        = var.namespace
  create_namespace = "true"

  values = [
    file("${var.helm_path}/values.yaml")
  ]

  set {
    name  = "image.tag"
    value = var.image_tag
  }

  set {
    name  = "aws.trustProfile"
    value = aws_rolesanywhere_profile.trust_profile.arn
  }

  set {
    name  = "aws.trustAnchor"
    value = data.aws_ssm_parameter.trust_anchor.value
  }

  set {
    name  = "aws.role"
    value = aws_iam_role.trust_profile_role.arn
  }

  set {
    name  = "database.url"
    value = var.database_url
  }

  set {
    name  = "database.user"
    value = var.database_user
  }

  set {
    name  = "bucket.name"
    value = var.bucket_name
  }
}
