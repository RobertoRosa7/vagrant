# Vagrant

#### Criar o arquivo Vagrantfile
`vagrant init hashicorp/precise64`

#### Subir e criar a máquina virtual
`vagrant up`

#### Conecte-se à máquina virtual 
`vagrant ssh`
#### Pare a máquina virtual
`vagrant halt`

#### Destruir
`vagrant destroy -f`

#### Connection Ip public SSH
`ssh -i id_bionic vagrant@192.168.1.24`

#### Configure new vm
```
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"
end
```

#### Configure forward port
```
Vagrant.configure("2") do |config|
  config.vm.network "forwarded_port", guest: 80, host: 8089
end
```

#### Configure IP Static
```
Vagrant.configure("2") do |config|
  config.vm.network "public_network", ip: "192.168.1.24"
end
```

#### Configure Shell Commands
```
Vagrant.configure("2") do |config|
  config.vm.provision "shell", inline: "cat /configs/id_bionic.pub >> .ssh/authorized_keys"
end
```

#### Configure Server DHCP
```
Vagrant.configure("2") do |config|
  config.vm.network "public_network", type: "dhcp"
end
```

#### Configure Create Folter
```
Vagrant.configure("2") do |config|
  config.vm.synced_folder "./configs", "/configs"
  config.vm.synced_folder ".", "/vagrant", disabled: true
end
```

#### Create database
```
$script_mysql = <<-SCRIPT
apt-get update && \
apt-get install -y mysql-server-5.7 && \
mysql -e "create user 'phpuser'@'%' identified by 'pass';"
SCRIPT
```

#### Exec script
```
Vagrant.configure("2") do |config|
  config.vm.provision "shell", inline: $script_mysql
end
```

#### Redefined settings MySQL
```
Vagrant.configure("2") do |config|
  config.vm.provision "shell", inline: "cat /configs/mysqld.cnf > /etc/mysql/mysql.conf.d/mysqld.cnf"
end
```

#### Restart Service MySQL
```
Vagrant.configure("2") do |config|
  config.vm.provision "shell", inline: "service mysql restart"
end
```

#### List and Remove
```
vagrant box list
vagrant box prune
vagrant box remove hashicorp/precise64
```


#### Configure Complete
```
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"
  config.vm.network "forwarded_port", guest: 80, host: 8089
  config.vm.network "public_network", ip: "192.168.1.24"
  config.vm.network "public_network", type: "dhcp"
  config.vm.provision "shell", inline: "cat /configs/id_bionic.pub >> .ssh/authorized_keys"
  config.vm.synced_folder "./configs", "/configs"
  config.vm.synced_folder ".", "/vagrant", disabled: true
end
```