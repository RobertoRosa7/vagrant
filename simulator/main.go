package main

// import "github.com/codeedu/imersaofsfc2-simulator/app/routes"
import "github.com/codeedu/imersaofsfc2-simulator/infra/kafka"
import "fmt"
import "github.com/joho/godotenv"
import "log"

func init() {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	} 
}


func main() {
	producer := kafka.NewKafkaProducer()
	kafka.Publish("ola", "readtest", producer)
	// route := route.Route{
	// 	ID: "1",
	// 	ClientID: "1",
	// }

	// route.LoadPositions()
	// stringjson, _ := route.ExportJsonPositions()
	// fmt.Println(stringjson[0])
}