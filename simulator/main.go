package main

import route "github.com/codeedu/imersaofsfc2-simulator/app/routes"
import "fmt"

func main() {
	route := route.Route{
		ID: "1",
		ClientID: "1"
	}

	route.LoadPositions()
	stringjson, _ := route.ExportJsonPositions()
	fmt.Println(stringjson[0])
}