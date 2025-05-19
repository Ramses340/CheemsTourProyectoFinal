package mx.itson.cheemstour.entities

class Weather {
    var name:String? = null
    var timezone: Int = 0
    var dt : Int = 0
    var main: Main? = null
    var wind: Wind? = null
    var clouds: Clouds? = null
    var sys: Sys? = null
    var weather: List<WeatherDescription>? = null
    var visibility: Int = 0
}

class Main {
    var temp: Double = 0.0
    var feels_like: Double = 0.0
    var temp_min: Double = 0.0
    var temp_max: Double = 0.0
    var pressure: Int = 0
    var humidity: Int = 0
}

class Wind {
    var speed: Double = 0.0
    var deg: Int = 0
    var gust: Double = 0.0
}

class Clouds {
    var all: Int = 0
}

class Sys {
    var sunrise: Long = 0
    var sunset: Long = 0
}

class WeatherDescription {
    var main: String? = null
    var description: String? = null
    var icon: String? = null
}
