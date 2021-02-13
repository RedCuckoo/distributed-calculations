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

func propertyCounter(mutex *sync.Mutex,buffer *int, waitGroup *sync.WaitGroup) {
	propertyCounter := 0
	receivedProperty := 0
	for {
		for {
			mutex.Lock()
			if *buffer >0{
				receivedProperty = 1
				*buffer--
				mutex.Unlock()
				break
			}else if *buffer == -1{
				receivedProperty = -1
				mutex.Unlock()
				break
			}else{
				//fmt.Println("Property Counter waits for property")
			}
			mutex.Unlock()
		}

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

func truckLoader(inputMutex *sync.Mutex,outputMutex *sync.Mutex,inputBuffer *int, outputBuffer *int, waitGroup *sync.WaitGroup) {
	receivedProperty := 0

	defer waitGroup.Done()

	for {
		for {
			inputMutex.Lock()
			if *inputBuffer >0 {
				receivedProperty = 1
				*inputBuffer--
				inputMutex.Unlock()
				break
			}else if *inputBuffer == -1{
				receivedProperty = -1
				inputMutex.Unlock()
				break
			}else {
				//fmt.Println("Truck Loader waits for property")
			}
			inputMutex.Unlock()
		}

		if receivedProperty == -1 {
			break
		}

		fmt.Println("Truck Loader received 1 item")

		time.Sleep(time.Millisecond * 100)

		outputMutex.Lock()
		*outputBuffer++
		outputMutex.Unlock()

		fmt.Println("Truck Loader carried 1 item")
	}

	fmt.Println("Truck Loader is done")

	for {
		//fmt.Println("Truck Loader waiting to say that it's over")
		outputMutex.Lock()
		if *outputBuffer == 0{
			*outputBuffer = -1
			outputMutex.Unlock()

			fmt.Println("Truck Loader says that it's over")

			break
		}
		outputMutex.Unlock()
	}
}

func storageRobber(mutex *sync.Mutex, buffer *int, propertyAmount int, waitGroup *sync.WaitGroup) {
	defer waitGroup.Done()

	for propertyAmount > 0 {
		propertyAmount--

		time.Sleep(time.Millisecond * 10)

		mutex.Lock()
		*buffer++
		mutex.Unlock()

		fmt.Println("Storage Robber carried 1 item")
	}

	fmt.Println("Storage Robber is done")

	for{
		//fmt.Println("Storage Robber waiting to say that it's over")

		mutex.Lock()
		if *buffer == 0{
			*buffer = -1
			mutex.Unlock()

			fmt.Println("Storage Robber says that it's over")
			break
		}
		mutex.Unlock()
	}
}

func rob() {
	propertyAmount := generatePropertyAmount()

	fmt.Printf("%d items in the storage to rob\n", propertyAmount)

	storageLoaderProperty := 0
	loaderCounterProperty := 0
	var storageLoaderMutex sync.Mutex
	var loaderCounterMutex sync.Mutex

	var waitGroup sync.WaitGroup
	waitGroup.Add(3)

	go storageRobber(&storageLoaderMutex,&storageLoaderProperty, propertyAmount, &waitGroup)
	go truckLoader(&storageLoaderMutex,&loaderCounterMutex,&storageLoaderProperty,&loaderCounterProperty, &waitGroup)
	go propertyCounter(&loaderCounterMutex,&loaderCounterProperty, &waitGroup)

	waitGroup.Wait()
}

func main() {
	rob()
}
