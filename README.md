# Vagrant

#### Criar o arquivo Vagrantfile
`vagrant init hashicorp/precise64`
#### Subir e criar a máquina virtual
`vagrant up`

#### Conecte-se à máquina virtual 
`vagrant ssh`
#### Pare a máquina virtual
`vagrant halt`


#### Configure forward
```
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"
  config.vm.network "forwarded_port", guest: 80, host: 8089
end
```
