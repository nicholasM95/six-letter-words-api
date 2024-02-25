data "aws_ssm_parameter" "trust_anchor" {
  name = "/nicholasmeyers/cluster/trust/anchor"
}

data "aws_iam_policy_document" "rolesanywhere_profile_role_policy_document" {
  statement {
    effect = "Allow"

    principals {
      type        = "Service"
      identifiers = ["rolesanywhere.amazonaws.com"]
    }

    actions = [
      "sts:AssumeRole",
      "sts:TagSession",
      "sts:SetSourceIdentity"
    ]

    condition {
      test     = "StringLike"
      variable = "aws:PrincipalTag/x509SAN/URI"
      values = [
        "spiffe://nicholas.home.cluster/*/sa/*"
      ]
    }

    condition {
      test     = "ArnEquals"
      variable = "aws:SourceArn"
      values = [
        data.aws_ssm_parameter.trust_anchor.value
      ]
    }
  }
}

data "aws_iam_policy_document" "rolesanywhere_profile_role_session_policy_document" {
  statement {
    effect = "Allow"

    actions = [
      "s3:GetObject",
      "s3:PutObject",
    ]

    resources = [
      "${var.bucket_arn}/*"
    ]

    condition {
      test     = "StringLike"
      variable = "aws:PrincipalTag/x509SAN/URI"
      values = [
        "spiffe://nicholas.home.cluster/ns/${var.namespace}/sa/${var.name}"
      ]
    }
  }

  statement {
    effect = "Allow"

    actions = [
      "rds-db:connect"
    ]

    resources = [
      "arn:aws:rds-db:${var.region}:${var.account}:dbuser:${var.database_resource_id}/${var.database_user}"
    ]

    condition {
      test     = "StringLike"
      variable = "aws:PrincipalTag/x509SAN/URI"
      values = [
        "spiffe://nicholas.home.cluster/ns/${var.namespace}/sa/${var.name}"
      ]
    }
  }
}
