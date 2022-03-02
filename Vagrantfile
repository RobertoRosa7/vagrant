$script_mysql = <<-SCRIPT
  apt-get update && \
  apt-get install -y mysql-server-5.7 && \
  mysql -e "create user 'web'@'%' identified by 'web';"
SCRIPT

Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"
  config.vm.box_download_insecure = true
  
  config.vm.provider "virtualbox" do |vb|
    vb.memory = 512
    vb.cpus = 1
  end

  # config.vm.define "mysqldb" do |mysql|
  #   mysql.vm.network "public_network", ip: "192.168.1.24"
  #   mysql.vm.provision "shell", inline: "cat /configs/id_bionic.pub >> .ssh/authorized_keys"
  #   mysql.vm.provision "shell", inline: $script_mysql
  #   mysql.vm.provision "shell", inline: "cat /configs/mysqld.cnf > /etc/mysql/mysql.conf.d/mysqld.cnf"
  #   mysql.vm.provision "shell", inline: "service mysql restart"
  #   mysql.vm.synced_folder "./configs", "/configs"
  #   mysql.vm.synced_folder ".", "/vagrant", disabled: true
  # end

  config.vm.define "web" do |web|
    web.vm.network "forwarded_port", guest: 8888, host:8888
    web.vm.network "public_network", ip: "192.168.1.25"
    
    config.vm.provider "virtualbox" do |vb|
      vb.memory = 1024
      vb.cpus = 2
      vb.name = "ubuntu_web"
    end

    web.vm.provision "shell", inline: "apt-get update && apt-get install -y puppet"
    
    web.vm.provision "puppet" do |puppet|
      puppet.manifests_path = "./configs/manifests"
      puppet.manifests_file = "web.pp"
    end

  end

  config.vm.define "mysqlserver" do |mysqlserver|
    mysqlserver.vm.network "public_network", ip: "192.168.1.22"
    mysqlserver.vm.provision "shell", inline: "cat /vagrant/configs/id_bionic.pub >> .ssh/authorized_keys"
  end

  config.vm.define "ansible" do |ansible|
    ansible.vm.network "public_network", ip: "192.168.1.26"
    ansible.vm.provision "shell", inline: "cp /vagrant/id_bionic /home/vagrant && \
    chmod 600 /home/vagrant/id_bionic && \
    chown vagrant:vagrant /home/vagrant/id_bionic"
    
    ansible.vm.provision "shell", inline: "apt-get update && \
    apt-get install -y software-properties-common && \
    apt-add-repository --yes --update ppa:ansible/ansible && \
    apt-get install -y ansible"

    ansible.vm.provision "shell", inline: "ansible-playbook -i /vagrant/configs/ansible/hosts /vagrant/configs/ansible/playbook.yml"
  end

  config.vm.define "memcached" do |memcached|
    memcached.vm.box = "centos/7"
    memcached.vm.provider "virtualbox" do |vb|
      vb.memory = 512
      vb.cpus = 1
      vb.name = "centos_memcached"
    end
  end

  config.vm.define "dockerhost" do |dockerhost|
    dockerhost.vm.provider "virtualbox" do |vb|
      vb.memory = 512
      vb.cpus = 1
      vb.name = "ubuntu_docker"
    end

    dockerhost.vm.provision "shell", inline: "apt-get update && apt-get install -y docker.io"
  end

end
