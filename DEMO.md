# Demo Updates & Troubleshooting

## Environment

All infrastructure components are listed under the `terraform`
directory. They will be in AWS `us-west-2`.

To create and update infrastructure, you need to have access
to Terraform Cloud. The state of resources is stored in:

- Organization: `hashicorp-team-da-beta`
- Workspace: `fabric-hashicraft-mod`

The workspace uses *remote execution*, which means you will need
to push your AWS credentials to the workspace using Doormat.

```shell
doormat aws --account $DA_AWS_ACCOUNT_ID \
  tf-push --organization hashicorp-team-da-beta \
  --workspace fabric-hashicraft-mod
```

It creates the following resources:

- Elastic Load Balancer
- Autoscaling Group for Minecraft Server
- Security Groups

## Minecraft Server Troubleshooting

The Minecraft server runs via Shipyard on a virtual machine
in AWS.

If you need to access the server for troubleshooting,
you can SSH into the machine using `doormat session`.

1. Find the instance ID in the Developer Advocate AWS account.
   It should be a running instance called `hashicraft-server`.

1. SSH into the machine.
   ```shell
   doormat session --account $DA_AWS_ACCOUNT \
       --region us-west-2 --ssh --target $INSTANCE_ID
   ```

1. You will need to log into the root environment to examine
   any of the components.
   ```shell
   sudo su
   ```

1. All the services required for HashiCraft (including Nomad, Consul,
   Vault, Boundary, and more) run in containers. You can list them using
   Docker.
   ```shell
   docker ps
   ```

## Minecraft Known Problems

1. Sometimes, minecarts will go rogue and cause a bit of a mess around the
   track. To solve this problem, you can delete all carts around the track.
   1. Open chat by pressing `T`.
   1. Destroy all HashiCraft carts
      ```shell
      /kill @e[type==hashicraft.app_minecart]
      ```