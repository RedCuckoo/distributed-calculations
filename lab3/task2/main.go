package main

import (
	"fmt"
	"math/rand"
	"sort"
	"sync"
	"time"
)

const(
	DAY_TIME = 10000000
	CUSTOMER_AMOUNT = 10
	MAX_HAIR_CUTTING_TIME = int(DAY_TIME / CUSTOMER_AMOUNT)
)

var customerID = 0

type Customer struct{
	id int
	arrivalTime int
	hairCuttingTime int
}

func barberBehave(customer *Customer, queue *[]Customer, queueMutex *sync.Mutex, barberIsAsleep *bool, barberMutex *sync.Mutex, waitGroup *sync.WaitGroup){
	defer waitGroup.Done()

	barberMutex.Lock()

	fmt.Println("Barber woke up")
	*barberIsAsleep = false

	barberMutex.Unlock()

	fmt.Printf("Barber started cutting hair of customer #%d\n", customer.id)

	time.Sleep(time.Duration(customer.hairCuttingTime))

	fmt.Printf("Barber finished cutting hair of customer #%d\n", customer.id)

	for{
		queueMutex.Lock()
		if len(*queue) == 0{
			barberMutex.Lock()
			*barberIsAsleep = true

			fmt.Println("Barber went to sleep")

			barberMutex.Unlock()

			queueMutex.Unlock()
			return
		}else{
			currentCustomer := (*queue)[0]
			*queue = (*queue)[1:]
			queueMutex.Unlock()

			fmt.Printf("Barber started cutting hair of customer #%d\n", currentCustomer.id)

			time.Sleep(time.Duration(currentCustomer.hairCuttingTime))

			fmt.Printf("Barber finished cutting hair of customer #%d\n", currentCustomer.id)
		}
	}
}

func customerBehave(customer *Customer, queue *[]Customer, queueMutex *sync.Mutex, barberIsAsleep *bool, barberMutex *sync.Mutex, waitGroup *sync.WaitGroup){
	defer waitGroup.Done()

	barberMutex.Lock()
	if *barberIsAsleep {
		waitGroup.Add(1)

		fmt.Printf("Customer #%d woke up the barber\n", customer.id)

		go barberBehave(customer, queue,queueMutex,barberIsAsleep,barberMutex, waitGroup)
	} else{
		queueMutex.Lock()

		fmt.Printf("Customer #%d stood to the queue\n", customer.id)

		*queue = append(*queue, *customer)
		queueMutex.Unlock()
	}
	barberMutex.Unlock()
}

func simulateBarberShop(){
	var queue []Customer
	var queueMutex sync.Mutex

	var customers []Customer
	currentCustomer := 0

	barberIsAsleep := true
	var barberMutex sync.Mutex

	rand.Seed(time.Now().UnixNano())

	for i:= 0; i < CUSTOMER_AMOUNT; i++{
		customers = append(customers, Customer{arrivalTime: rand.Intn(DAY_TIME), hairCuttingTime: rand.Intn(MAX_HAIR_CUTTING_TIME), id: customerID})
		customerID++
	}

	//sort customers by arrival time
	sort.Slice(customers, func(i,j int) bool {
		return customers[i].arrivalTime < customers[j].arrivalTime
	})

	var waitGroup sync.WaitGroup
	defer waitGroup.Wait()

	for i:=0; i < DAY_TIME; i++{
		if currentCustomer == CUSTOMER_AMOUNT{
			break
		}

		if customers[currentCustomer].arrivalTime == i{
			waitGroup.Add(1)

			fmt.Printf("Customer #%d came to barber shop\n", customers[currentCustomer].id)

			go customerBehave(&customers[currentCustomer],&queue, &queueMutex, &barberIsAsleep, &barberMutex,&waitGroup)

			currentCustomer++
		}
	}
}

func main() {
	simulateBarberShop()
}
