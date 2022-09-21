#!/bin/bash

apt update

## Set up EBS volume for Docker

mkfs -t xfs /dev/nvme1n1
mkdir -p /var/lib/docker
mount /dev/nvme1n1 /var/lib/docker

## Install HashiCorp tools
apt install -y gnupg software-properties-common

wget -O- https://apt.releases.hashicorp.com/gpg | \
    gpg --dearmor | \
    sudo tee /usr/share/keyrings/hashicorp-archive-keyring.gpg

gpg --no-default-keyring \
    --keyring /usr/share/keyrings/hashicorp-archive-keyring.gpg \
    --fingerprint

echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] \
    https://apt.releases.hashicorp.com $(lsb_release -cs) main" | \
    sudo tee /etc/apt/sources.list.d/hashicorp.list

apt update

apt install -y terraform consul vault boundary nomad

## Install Docker & Docker-Compose
apt install -y \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

apt update

apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

## Install Shipyard and dependencies

echo "deb [trusted=yes] https://apt.fury.io/shipyard-run/ /" | sudo tee -a /etc/apt/sources.list.d/fury.list
apt update

apt install -y shipyard git

## Create Shipyard blueprint
shipyard run github.com/HashiCraft/fabric-hashicraft-mod//shipyard

if [ $? -eq 1 ]
then
    shipyard run github.com/HashiCraft/fabric-hashicraft-mod//shipyard
fi