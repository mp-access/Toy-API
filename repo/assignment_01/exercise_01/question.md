# Task: UZH Airlines (5 Points)

In this task you will model the information system of aircrafts and control tower of an airport. Let’s begin with the class `Aircraft` after which all types of planes will be modeled. Each aircraft can carry a certain `number_of_passengers` and has a model `name`. Save these as protected in- stance variables as each plane subclass should have access to these. Add getter methods to access these variables:

- `get_number_of_passengers`

- `get_name`

To keep things simple for now, we will only add two different types of aircrafts, but more may be needed in future. Add classes `IntercontinentalAircraft` and `ShortHaulAircraft`. Each `IntercontinentalAircraft` has a certain `cargo_hold` measured in tonnes in addition to the previously defined instance variables. The cargo hold should be defined as a private instance variable.
Because of reasons, each `ShortHaulAircraft` has additionally a serial number which is unique for each plane (hint: class variable). Add a getter `get_serial_number`.
The on-board system on both `IntercontinentalAircraft` and `ShortHaulAircraft` can estimate the fuel consumption for a certain trip in kilometers. Add a method `calculate_amount_of_fuel(self, km)` which will compute how much fuel is necessary to complete the trip. The aircrafts however
compute the amount differently:

- `IntercontinentalAircraft`: a long haul aircraft consumes 0.25 liters of fuel per km per passenger, plus 2 liters per km per tonne of cargo
- `ShortHaulAircraft` are more efficient: 0.1 liters per km per passenger

Additionally, each airplane should hold a manifest of what they are transporting for customs
(define the `manifest` property which returns a string):

- `IntercontinentalAircraft: Intercontinental flight {name}: passenger count {passengers}, cargo load {load}`, e.g. `Intercontinental flight Boeing-747: passenger count
  500, cargo load 100`
- `ShortHaulAircraft: Short haul flight serial number {serial number}, name {name}: passenger count {passengers}`, e.g. `Short haul flight serial number 1, name Airbus-A220: passenger count 85`

Lastly, add the control tower `ControlTower` which keeps an eye on a list of aircrafts. It should be possible to add aircrafts to observe (`add_aircraft(self, aircraft)`) and list all flights and their manifests (`get_manifests` which returns a list containing the manifest of each airplane)

**Task**: Implement the classes, following the instructions above.



```python
if __name__ == '__main__':
    intercontinental_flight = IntercontinentalAircraft(500, "Boeing-747", 100)
    short_haul_flight = ShortHaulAircraft(110, "Airbus-A220")
    short_haul_flight2 = ShortHaulAircraft(85, "Airbus-A220")

    assert short_haul_flight.get_serial_number() == 0
    assert short_haul_flight2.get_serial_number() == 1

    assert short_haul_flight.get_number_of_passengers() == 110
    assert short_haul_flight.get_name() == "Airbus-A220"

    assert intercontinental_flight.get_number_of_passengers() == 500
    assert intercontinental_flight.get_name() == "Boeing-747"

    assert intercontinental_flight.calculate_amount_of_fuel(10000) == 3250000.
    assert short_haul_flight.calculate_amount_of_fuel(250) == 2750.

    assert intercontinental_flight.manifest == "Intercontinental flight Boeing-747: passenger count 500, cargo load 100"
    assert short_haul_flight2.manifest == "Short haul flight serial number 1, name Airbus-A220: passenger count 85"

    tower = ControlTower()
    tower.add_aircraft(intercontinental_flight)
    tower.add_aircraft(short_haul_flight)
    tower.add_aircraft(short_haul_flight2)

    air_traffic_report = tower.get_manifests()
    for aircraft in air_traffic_report:
        print(aircraft)

    # prints:
    # Intercontinental flight Boeing-747: passenger count 500, cargo load 100
    # Short haul flight serial number 0, name Airbus-A220: passenger count 110
    # Short haul flight serial number 1, name Airbus-A220: passenger count 85

```

