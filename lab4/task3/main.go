package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const (
	//must be > 1
	CITIES_AMOUNT = 10
	MAX_PRICE     = 100
)

var cityID = 0

type Money struct {
	dollars int
	cents   int
}

type City struct {
	id    int
	trips []Trip
}

type Trip struct {
	price       Money
	destination City
}

func getRandMoney() Money {
	return Money{dollars: rand.Intn(MAX_PRICE), cents: rand.Intn(100)}
}

func getRandCityExcept(cities []City, exceptCity City) City {
	var city City
	city.id = -1

	for city.id == exceptCity.id || city.id == -1 {
		city = cities[rand.Intn(len(cities))]
	}

	return city
}

func removeElementFromSliceByValue(cities []City, toRemove City) []City {
	citiesResult := make([]City, len(cities)-1)

	for i := 0; i < len(cities); i++ {
		if cities[i].id == toRemove.id {
			if i+1 != len(cities) {
				citiesResult = append(cities[:i], cities[(i+1):]...)
			} else {
				citiesResult = append(cities[:i])
			}
		}
	}

	if len(citiesResult) == 0 {
		return cities
	}

	return citiesResult
}

func generateCities() []City {
	var cities []City

	for i := 0; i < CITIES_AMOUNT; i++ {
		cities = append(cities, City{id: cityID})
		cityID++
	}

	if cities == nil {
		return cities
	}

	for i := 0; i < CITIES_AMOUNT; i++ {
		connectionsAmount := rand.Intn(CITIES_AMOUNT - 1)
		connectionsAmount++

		connectionsAmount -= len(cities[i].trips)

		if connectionsAmount <= 0 {
			continue
		}

		citiesCopy := make([]City, len(cities))
		copy(citiesCopy, cities)

		for j := 0; j < len(cities[i].trips); j++ {
			citiesCopy = removeElementFromSliceByValue(citiesCopy, cities[i].trips[j].destination)
		}

		if len(citiesCopy) == 1 {
			continue
		}

		for j := 0; j < connectionsAmount; j++ {
			destinationCity := getRandCityExcept(citiesCopy, cities[i])
			var destinationCityIndex int
			for k := 0; k < CITIES_AMOUNT; k++ {
				if destinationCity.id == cities[k].id {
					destinationCityIndex = k
					break
				}
			}
			price := getRandMoney()

			cities[i].trips = append(cities[i].trips, Trip{price: price,
				destination: destinationCity})
			cities[destinationCityIndex].trips = append(destinationCity.trips, Trip{price: price,
				destination: cities[i]})

			citiesCopy = removeElementFromSliceByValue(citiesCopy, destinationCity)
		}
	}

	return cities
}

func printCities(cities []City) {
	for i := 0; i < CITIES_AMOUNT; i++ {
		fmt.Printf("City #%d\n", cities[i].id)
		for j := 0; j < len(cities[i].trips); j++ {
			fmt.Printf("\tdestination #%d, costs %d dollars %d cents\n",
				cities[i].trips[j].destination.id,
				cities[i].trips[j].price.dollars,
				cities[i].trips[j].price.cents)
		}
	}
}

//------------------------------------------MAIN FUNCTIONS------------------------------------

func changePrice(cities []City, departureCityId int, destinationCityId int, setPrice Money,
		mutex *sync.RWMutex, waitGroup *sync.WaitGroup) {
	defer waitGroup.Done()
	mutex.Lock()
	for k := 0; k < 2; k++ {
		for i := 0; i < len(cities[departureCityId].trips); i++ {
			if cities[departureCityId].trips[i].destination.id == destinationCityId {
				cities[destinationCityId].trips[i].price = setPrice
			}
		}
		temp := destinationCityId
		destinationCityId = departureCityId
		departureCityId = temp
	}
	mutex.Unlock()
}

func addTrip(cities []City, departureCityId int, tripToAdd Trip, mutex *sync.RWMutex, waitGroup sync.WaitGroup){
	defer waitGroup.Done()

	mutex.Lock()

	cities[departureCityId].trips = append(cities[departureCityId].trips, tripToAdd)
	cities[tripToAdd.destination.id].trips = append(cities[tripToAdd.destination.id].trips,
		Trip{destination: cities[departureCityId], price: tripToAdd.price})

	mutex.Unlock()
}

func deleteTrip(cities []City, departureCityId int, tripId int, mutex *sync.RWMutex, waitGroup *sync.WaitGroup) {
	defer waitGroup.Done()

	mutex.Lock()

	for i := 0; i < 2; i++ {
		if len(cities[departureCityId].trips) == 1{
			destinationCityId := cities[departureCityId].trips[tripId].destination.id
			cities[departureCityId].trips = cities[departureCityId].trips[:0]
			departureCityId = destinationCityId
			continue
		}

		cities[departureCityId].trips = append(cities[departureCityId].trips[:tripId],
			cities[departureCityId].trips[tripId+1:]...)

		if i == 1 {
			break
		}

		departureCityId = cities[departureCityId].trips[tripId].destination.id
	}

	mutex.Unlock()
}

func deleteCity(cities []City, cityToDeleteId int, mutex *sync.RWMutex, waitGroup *sync.WaitGroup){
	defer waitGroup.Done()

	mutex.Lock()

	cities = append(cities[:cityToDeleteId], cities[cityToDeleteId+1:]...)

	mutex.Unlock()
}

func replaceCity(cities []City, cityToReplace City, cityToAdd City, mutex sync.Mutex, waitGroup sync.WaitGroup){
	defer waitGroup.Done()

	mutex.Lock()

	cityToAdd.id = cityToReplace.id

	cities = append(cities[:cityToReplace.id],cityToAdd)
	cities = append(cities[:cityToReplace.id],cities[cityToReplace.id:]...)

	mutex.Unlock()
}

func recursiveFindWay(cities []City, previousId int, startCityId int, endCityId int, wayCityId *[]int ) bool{
	if startCityId == endCityId{
		*wayCityId = append(*wayCityId, startCityId)
		return true
	}

	for i := 0; i < len(cities[startCityId].trips); i++{
		if previousId == cities[startCityId].trips[i].destination.id{
			continue
		}

		if recursiveFindWay(cities, startCityId, cities[startCityId].trips[i].destination.id,endCityId, wayCityId){
			*wayCityId = append(*wayCityId, startCityId)
			return true
		}
	}

	return false
}

func findWay(cities []City, startCityId int, endCityId int, mutex *sync.RWMutex, waitGroup *sync.WaitGroup){
	defer waitGroup.Done()

	mutex.RLock()

	for i := 0; i < len(cities[startCityId].trips); i++{
		if cities[startCityId].trips[i].destination.id == endCityId{
			cost := cities[startCityId].trips[i].price
			fmt.Printf("Direct way exists, costs %d dollars and %d cents\n", cost.dollars,cost.cents )
			mutex.RUnlock()
			return
		}
	}

	var wayId []int

	recursiveFindWay(cities, startCityId, startCityId, endCityId, &wayId)

	mutex.RUnlock()

	fmt.Print("Indirect way:\n")
	for i := len(wayId)-1; i >0 ; i--{
		var trip Trip

		for j:= 0; j < len (cities[wayId[i]].trips); j++{
			if cities[wayId[i]].trips[j].destination.id == wayId[i-1]{
				trip = cities[wayId[i]].trips[j]
			}
		}

		fmt.Printf("\tfrom city #%d to city #%d, costs %d dollars and %d cents\n",
			wayId[i], wayId[i-1],trip.price.dollars, trip.price.cents)
	}
}

func simulateBusTravelSystem() {
	cities := generateCities()

	printCities(cities)

	var mutex sync.RWMutex
	var waitGroup sync.WaitGroup
	waitGroup.Add(3)
	go changePrice(cities, 0, 1, Money{dollars: 2, cents: 4}, &mutex, &waitGroup)
	go deleteTrip(cities, 0, 0, &mutex, &waitGroup)
	go findWay(cities, 0,1, &mutex, &waitGroup)
	waitGroup.Wait()
}

func main() {
	rand.Seed(time.Now().UTC().UnixNano())
	simulateBusTravelSystem()
}