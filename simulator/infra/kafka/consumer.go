package kafka
import ckafka "github.com/confluentinc/confluent-kafka-go/kafka"
import "os"
import "fmt"
import "log"

type KafkaConsumer struct {
	MsgChan chan *ckafka.Message
}

func NewKafkaConsumer(msgChan chan *ckafka.Message) *KafkaConsumer {
	return &KafkaConsumer{
		MsgChan: msgChan,
	}
}

func(k *KafkaConsumer) Consume() {
	configMap := &ckafka.configMap{
		"bootstrap.servers":os.Getenv("kafkaBootstrapServers"),
		"group.id":os.Getenv("KafkaConsumerGroupId"),
	}
	c, err := ckafka.NewConsumer(configMap)
	
	if err != nil {
		log.Fatalf("error consuming kafka message:" + err.Error())
	}
	topics := []string{os.Getenv("kafkaReadTopic")}
	c.subscribeTopics(topics, nil)
	fmt.Println("Kafka consumer has been started")

	for {
		msg, err := c.ReadMessage(-1)

		if err == nil {
			k.MsgChan <- msg
		}
	}
}