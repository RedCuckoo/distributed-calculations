package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const (
	MAX_AMOUNT_PROPERTY = 10
)

func generatePropertyAmount() int {
	rand.Seed(time.Now().UnixNano())
	return rand.Intn(MAX_AMOUNT_PROPERTY)
}

func propertyCounter(inputChannel chan int, waitGroup *sync.WaitGroup) {
	propertyCounter := 0
	receivedProperty := 0
	for {
		receivedProperty = <-inputChannel

		if receivedProperty == -1 {
			break
		}

		fmt.Println("Property Counter received 1 item")

		time.Sleep(time.Millisecond * 10)

		propertyCounter++

		fmt.Println("Property Counter counted 1 item")
	}

	fmt.Println("Property Counter is done")

	fmt.Printf("%d items were loaded on the truck\n", propertyCounter)

	waitGroup.Done()
}

func truckLoader(inputChannel chan int, outputChannel chan int, waitGroup *sync.WaitGroup) {
	receivedProperty := 0

	for {
		receivedProperty = <-inputChannel

		if receivedProperty == -1 {
			break
		}

		fmt.Println("Truck Loader received 1 item")

		time.Sleep(time.Millisecond * 100)

		outputChannel <- 1

		fmt.Println("Truck Loader carried 1 item")
	}

	fmt.Println("Truck Loader is done")

	outputChannel <- -1

	waitGroup.Done()
}

func storageRobber(outputChannel chan int, propertyAmount int, waitGroup *sync.WaitGroup) {
	for propertyAmount > 0 {
		propertyAmount--

		time.Sleep(time.Millisecond * 10)

		outputChannel <- 1

		fmt.Println("Storage Robber carried 1 item")
	}

	fmt.Println("Storage Robber is done")

	outputChannel <- -1

	waitGroup.Done()
}

func rob() {
	propertyAmount := generatePropertyAmount()

	fmt.Printf("%d items in the storage to rob\n", propertyAmount)

	storageOutputChannel := make(chan int, 1)
	truckInputChannel := make(chan int, 1)

	var waitGroup sync.WaitGroup
	waitGroup.Add(3)

	go storageRobber(storageOutputChannel, propertyAmount, &waitGroup)
	go truckLoader(storageOutputChannel, truckInputChannel, &waitGroup)
	go propertyCounter(truckInputChannel, &waitGroup)

	waitGroup.Wait()
}

func main() {
	rob()
}
