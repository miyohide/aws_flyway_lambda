# 概要

Javaで実装したLambda関数です。S3にあるファイルを`/tmp`以下にコピーし、RDS for PostgreSQLに接続します。

# デプロイ

Code Buildを使ってLambdaにデプロイします。Lambdaの設定は`template.yml`が使われます。Code Buildは`buildspec.yml`が使われます。
