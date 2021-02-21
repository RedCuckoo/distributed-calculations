package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const(
	SPEED = 10000000000
	BEE_AMOUNT = 10
	MAX_COLLECT_TIME = 3 * SPEED
	SWALLOW_COUNT = 3
)

var beeID = 0

type Bee struct{
	id int
	collectTime int
}

func wakeUpBear(potMutex *sync.Mutex, pot *int, bearMutex *sync.Mutex, bearIsAsleep *bool){
	potMutex.Lock()
	time.Sleep(SWALLOW_COUNT * SPEED / 2)

	fmt.Println("Bear drunk the honey")

	*pot = 0
	bearMutex.Lock()
	*bearIsAsleep = true
	bearMutex.Unlock()
	potMutex.Unlock()
}

func collectHoney(potMutex *sync.Mutex, pot *int, bearMutex *sync.Mutex, bearIsAsleep *bool, bee Bee){
	currentHoney := false

	for{
		if !currentHoney{
			time.Sleep(time.Duration(bee.collectTime))
		}

		potMutex.Lock()
		if *pot < SWALLOW_COUNT {
			fmt.Printf("Bee #%d added to pot\n", bee.id)

			*pot++
			currentHoney = false

		}

		if *pot == SWALLOW_COUNT{
			bearMutex.Lock()
			if *bearIsAsleep{
				fmt.Printf("Bee #%d woke up the bear\n", bee.id)

				*bearIsAsleep = false
				go wakeUpBear(potMutex, pot,bearMutex ,bearIsAsleep)
			}else{
				fmt.Printf("Bee #%d wanted to wake up the bear but it was already up\n", bee.id)
				currentHoney = true
			}
			bearMutex.Unlock()
		}
		potMutex.Unlock()
	}
}

func honeyMoney() {
	var bearMutex sync.Mutex
	bearIsAsleep := true
	var potMutex sync.Mutex
	var pot int

	rand.Seed(time.Now().UnixNano())

	for i := 0; i < BEE_AMOUNT; i++{
		go collectHoney(&potMutex, &pot, &bearMutex, &bearIsAsleep, Bee{collectTime: rand.Intn(MAX_COLLECT_TIME), id: beeID})
		beeID++
	}

	select {}
}

func main(){
	honeyMoney()
}