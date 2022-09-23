resource "aws_s3_bucket" "hashicraft_backup" {
  bucket = "hashicraft-backup"

  tags = {
    Name = "hashicraft-backup"
  }
}

resource "aws_s3_bucket_acl" "hashicraft_backup" {
  bucket = aws_s3_bucket.hashicraft_backup.id
  acl    = "private"
}

resource "random_password" "hashicraft_backup" {
  length           = 16
  special          = true
  override_special = "!#$%&*()-_=+[]{}<>:?"
}

data "aws_iam_policy_document" "hashicraft_backup" {
  statement {
    effect = "Allow"

    actions = [
      "s3:PutObject*",
      "s3:ListBucket",
      "s3:GetObject*",
      "s3:DeleteObject*",
      "s3:GetBucketLocation"
    ]

    resources = [
      "${aws_s3_bucket.hashicraft_backup.arn}",
      "${aws_s3_bucket.hashicraft_backup.arn}/*"
    ]
  }
}

resource "aws_iam_policy" "hashicraft_backup" {
  name   = "hashicraft-backup"
  policy = data.aws_iam_policy_document.hashicraft_backup.json
}
