
enum class Operating {;

    companion object {
        var status = "running"
        var placeholderUnit = UnitMeasurements
        fun isN(string: String): Boolean {//  check if input is Number
            return string.matches("-?\\d+(\\.\\d+)?".toRegex())
        }
        fun isD(string: String): Boolean {//  check if input contains "degree"
            return string.contains("degree")
        }

        fun run(command: String) {
            val line = command.toLowerCase().split(" ")
            var ll = 0 // Line Length
            for (i in 0..line.lastIndex) { ll++ }
            if (line[0] == "exit") { status = "exit"; return }
            if (line[0] == "help") {
                placeholderUnit.helpMenu("help")
            } else if (line[0] == "f1") {
                placeholderUnit.helpMenu("f1")
            } else if (isN(line[0]) && ll == 4 && !isN(line[1]) && !isN(line[2]) && !isN(line[3])) {
                UnitMeasurements.convert(line[0].toDouble(), UnitMeasurements.getMeasureName(line[1]), UnitMeasurements.getMeasureName(line[3]))
            } else if (isN(line[0]) && ll == 5 && !isN(line[1]) && !isN(line[2]) && !isN(line[3]) && !isN(line[4])) {
                if (isD(line[1])) UnitMeasurements.convert(line[0].toDouble(), UnitMeasurements.getMeasureName(line[2]), UnitMeasurements.getMeasureName(line[4]))
                if (isD(line[3])) UnitMeasurements.convert(line[0].toDouble(), UnitMeasurements.getMeasureName(line[1]), UnitMeasurements.getMeasureName(line[4]))
            } else if (isN(line[0]) && ll == 6 && !isN(line[1]) && !isN(line[2]) && !isN(line[3]) && !isN(line[4]) && !isN(line[5]) && isD(line[1]) && isD(line[4])) {
                UnitMeasurements.convert(line[0].toDouble(), UnitMeasurements.getMeasureName(line[2]), UnitMeasurements.getMeasureName(line[5]))
            } else println("Parse error")
        }
    }
}

enum class UnitMeasurements(val suffixK: Double, val names: Array<String>, val state: String, val FahrAddition: Double) {
    METER(1.0, arrayOf("m", "meter", "meters"), "length", 0.0),
    KILOMETER(1000.0, arrayOf("km", "kilometer", "kilometers"), "length", 0.0),
    CENTIMETER(0.01, arrayOf("cm", "centimeter", "centimeters"), "length", 0.0),
    MILLIMETER(0.001, arrayOf("mm", "millimeter", "millimeters"), "length", 0.0),
    MILE(1609.35, arrayOf("mi", "mile", "miles"), "length", 0.0),
    YARD(0.9144, arrayOf("yd", "yard", "yards"), "length", 0.0),
    FEET(0.3048, arrayOf("ft", "foot", "feet"), "length", 0.0),
    INCH(0.0254, arrayOf("in", "inch", "inches"), "length", 0.0),

    GRAM(1.0, arrayOf("g", "gram", "grams"), "weight", 0.0),
    KILOGRAM(1000.0, arrayOf("kg", "kilogram", "kilograms"), "weight", 0.0),
    MILLIGRAM(0.001, arrayOf("mg", "milligram", "milligrams"), "weight", 0.0),
    POUND(453.592, arrayOf("lb", "pound", "pounds"), "weight", 0.0),
    OUNCE(28.3495, arrayOf("oz", "ounce", "ounces"), "weight", 0.0),

    CELSIUS(1.8, arrayOf("celsius", "degree Celsius", "degrees Celsius", "dc", "c", "degrees celsius", "degree Celsius"), "temp", 0.0),
    KELVIN(1.8, arrayOf("k", "Kelvin", "Kelvins", "kelvin", "kelvins"), "temp", 273.15),
    FAHRENHEIT(1.0, arrayOf("f", "degree Fahrenheit", "degrees Fahrenheit", "fahrenheit", "df", "degree fahrenheit", "degrees fahrenheit", "fahrenheits"), "temp", 32.0),
    ERROR(0.0, arrayOf("", "", "???", "It's just an ERROR value, dont bother with it :D"), "", 0.0);

    fun getPluralOrSingular(amount: Double) = when (amount) {
        1.0 -> this.names[1]
        else -> this.names[2]
    }
    companion object {
        fun getMeasureName(size: String): UnitMeasurements {
            for (enum in values()) {
                if (enum.names.contains(size)) return enum
            }
            return ERROR
        }
        fun convert(amount: Double, startMeasure: UnitMeasurements, resultMeasure: UnitMeasurements) { // || startMeasure.name == "KELVIN"
            if (startMeasure.state == resultMeasure.state && startMeasure != ERROR) { // && startMeasure != ERROR
                if ((startMeasure.state == "weight" || startMeasure.state == "length") && amount < 0) {
                    println("${startMeasure.state.capitalize()} shouldn't be negative")
                } else if ((startMeasure.name == "KELVIN" && amount < 0) || (startMeasure.name == "CELSIUS" && amount < -273.15) || (startMeasure.name == "FAHRENHEIT" && amount < -459.67)) {
                    println("Absolute Zero! Cant be colder than -273.15C or -459.67F or 0K")
                } else {
                    val result = (amount - startMeasure.FahrAddition) * startMeasure.suffixK / resultMeasure.suffixK + resultMeasure.FahrAddition
                    return println("$amount ${startMeasure.getPluralOrSingular(amount)} is $result ${resultMeasure.getPluralOrSingular(result)}")
                }
            } else if (startMeasure.state == resultMeasure.state && startMeasure == ERROR) {
                println("Conversion from ${startMeasure.names[2]} to ${resultMeasure.names[2]} is impossible")
            } else println("Conversion from ${startMeasure.names[2]} to ${resultMeasure.names[2]} is impossible")
        }
        fun helpMenu(line: String) {       /// help menu
            if (line == "help") {
                println("Hello! This is my uber unit converter. U can convert:\n" +
                        "Temperature        >   Fahrenheit into Celsius or Kelvins and vice versa.\n" +
                        "Length             >   Meter into Yards, Feet, Inches and vice versa.\n" +
                        "Weights            >   Grams into Pounds or Ounces and vice versa.\n" +
                        "Example Commands   >   'Value' of 'FirstMeasurement' into 'SecondMeasurement'\n" +
                        "Example Commands   >   42 degrees Celsius in Fahrenheit\n" +
                        "Example Commands   >   146 meters into inches\n" +
                        "U cant convert grams into meters though, so beware!\n" +
                        "Type   'F1'    for all possible units available to measure.\n")
            }
            if (line == "f1") {
                println("All commands can be Capital, LowerCase or CamelCase, it doesn't matter.")
                for (enum in values()) {
                    println("For ${enum.name.toLowerCase().capitalize()} - possible commands are:  >>  ${enum.names.joinToString()}")
                }
                println("All commands can be Capital, LowerCase or CamelCase, it doesn't matter.\n")
            }
        }
    }
}

fun main(args : Array<String>) {
    while (Operating.status == "running") {
        print("Enter what you want to convert (or exit): ")
        Operating.run(readLine()!!)
    }
}
