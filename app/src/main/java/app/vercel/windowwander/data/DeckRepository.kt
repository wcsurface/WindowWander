package app.vercel.windowwander.data

import app.vercel.windowwander.model.Deck
import app.vercel.windowwander.model.Prompt
import app.vercel.windowwander.model.Region

object DeckRepository {

    private fun p(
        id: String,
        name: String,
        description: String,
        category: String,
        isKidsEasy: Boolean = false,
        region: Region = Region.UNIVERSAL
    ) = Prompt(id, name, description, category, emptyList(), isKidsEasy, region, "road")

    // -------------------------------------------------------------
    // DECK A: CLASSIC ROAD TRIP (42 CONCISE PROMPTS)
    // -------------------------------------------------------------
    val DECK_A_PROMPTS = listOf(
        p("a1", "Yellow Traffic Sign", "A diamond or rectangular warning sign", "signs", true),
        p("a2", "Red Car or Truck", "Any red passenger vehicle or truck", "vehicles", true),
        p("a3", "Semi Truck / Big Rig", "Large 18-wheeler freight truck", "vehicles", true),
        p("a4", "Speed Limit Sign", "White rectangular highway speed sign", "signs", true),
        p("a5", "License Plate Out of State", "Plate from a non-neighboring state", "signs", true),
        p("a6", "School Bus", "Yellow school transportation bus", "vehicles", true),
        p("a7", "Gas Station Sign", "Brand logo or price sign for gas", "structures", true),
        p("a8", "Billboard", "Large advertising sign along the road", "signs", true),
        p("a9", "Exit Sign", "A green or blue highway exit sign", "signs"),
        p("a10", "Bridge", "Any road bridge or overpass structure", "structures"),
        p("a11", "Railroad Crossing", "A sign or barrier for train tracks", "signs"),
        p("a12", "Wind Turbine", "A giant white wind energy generator", "landscape"),
        p("a13", "Construction Equipment", "Excavator, bulldozer, or road roller", "equipment"),
        p("a14", "Tractor", "A farm tractor in a field or trailer", "equipment"),
        p("a15", "Farm Animals", "Cows, horses, or sheep in a field", "nature"),
        p("a16", "Water Tower", "A town or city water storage tower", "structures"),
        p("a17", "RV or Camper", "A motorhome, travel trailer, or pop-up", "vehicles"),
        p("a18", "Pickup Truck", "A full-size or compact pickup truck", "vehicles"),
        p("a19", "Car with Roof Rack", "Vehicle with cargo box or luggage rack", "vehicles"),
        p("a20", "Yellow Vehicle", "A yellow car, truck, or van", "vehicles"),
        p("a21", "Vehicle Towing Trailer", "A car or truck pulling a utility trailer", "vehicles"),
        p("a22", "Rest Area Sign", "A highway blue rest area or welcome center sign", "signs"),
        p("a23", "Toll Booth / Plaza", "A highway toll collection area or gantry", "structures"),
        p("a24", "Roadside Restaurant Sign", "Diner, fast food, or cafe logo sign", "signs"),
        p("a25", "Overpass", "A highway bridge passing overhead", "structures"),
        p("a26", "Tunnel", "A road tunnel through a hill or mountain", "structures"),
        p("a27", "Traffic Cone", "An orange safety cone along the road", "equipment"),
        p("a28", "Roadside Flag", "American flag or state flag visible from road", "signs"),
        p("a29", "EV Charging Station", "Electric vehicle charger or sign", "structures"),
        p("a30", "Scenic Overlook", "A designated pullover spot with views", "landscape"),
        p("a31", "Bicycles on Rack", "Car carrying bicycles on rear or roof", "vehicles"),
        p("a32", "Funny Bumper Sticker", "A bumper sticker that makes you smile", "fun"),
        p("a33", "Animal Road Sign", "Warning sign for deer, moose, or cattle", "signs"),
        p("a34", "Personalized License Plate", "A custom vanity license plate", "signs"),
        p("a35", "Fruit or Produce Stand", "A roadside farm stand selling fresh produce", "structures"),
        p("a36", "Police Vehicle", "Highway patrol or local police cruiser", "vehicles"),
        p("a37", "Delivery Truck", "FedEx, UPS, Amazon, or postal vehicle", "vehicles"),
        p("a38", "Road Crew Worker", "A worker in a high-visibility vest", "people"),
        p("a39", "Lake or River", "Water body clearly visible from the vehicle", "landscape"),
        p("a40", "Kayak or Canoe", "Car carrying a boat on the roof or trailer", "vehicles"),
        p("a41", "Speed Limit Sign", "A white rectangular speed limit sign", "signs"),
        p("a42", "Mile Marker", "A small green post indicating highway mileage", "signs")
    )

    // -------------------------------------------------------------
    // DECK B: WEIRD ROAD TRIP (42 EXACT UPDATED PROMPTS)
    // -------------------------------------------------------------
    val DECK_B_PROMPTS = listOf(
        p("b1", "Funny Bumper Sticker", "A sticker with an utterly baffling message", "weird"),
        p("b2", "Over-Accessorized Vehicle", "Car covered in chrome, antenna balls, or decals", "weird"),
        p("b3", "Funny Billboard Slogan", "A billboard featuring an unintentionally hilarious quote", "weird"),
        p("b4", "Cryptic Vanity Plate", "A vanity plate that takes 3 minutes to decipher", "weird"),
        p("b5", "Car Older Than Driver", "A vintage beat-up car driven by a teenager", "weird"),
        p("b6", "Giant Roadside Statue", "A giant paul bunyan, dinosaur, or rubber duck", "weird"),
        p("b7", "Hilarious Business Name", "A store name featuring a terrible pun", "weird"),
        p("b8", "Mismatched Body Panels", "Car with 3 different colored doors or fenders", "weird"),
        p("b9", "Custom Decorated Truck", "A truck painted with flames, dragons, or skulls", "weird"),
        p("b10", "Inflatable Advert character", "A giant waving inflatable man or mascot", "weird"),
        p("b11", "Bizarre Slogan Sign", "Sign advertising 'Free Ice' or 'Mystery Shack'", "weird"),
        p("b12", "Strange Attraction Sign", "Sign for World's Largest Ball of String or similar", "weird"),
        p("b13", "Dashboard Ducks", "Dashboard filled with bobbleheads or rubber ducks", "weird"),
        p("b14", "Hyper-Specific Shop", "A business selling ONLY left-handed scissors or rocks", "weird"),
        p("b15", "Unexpected Item Towed", "Vehicle towing a lawnmower, recliner, or hot tub", "weird"),
        p("b16", "Sign with Spelling Error", "A commercial sign with an obvious typo", "weird"),
        p("b17", "Farm Animals", "Billboard showing a dog or pig in a suit", "weird"),
        p("b18", "Comically Large Accessory", "Exhaust pipes larger than a trash can", "weird"),
        p("b19", "Unusual Color Building", "A bright pink, neon green, or purple store", "weird"),
        p("b20", "Double-Meaning Sign", "A road sign or advert open to funny interpretation", "weird"),
        p("b21", "Very Old Lady Driving", "A very old lady driving", "weird"),
        p("b22", "Anime Stickers", "Anime stickers on a car", "weird"),
        p("b23", "Lawnmowing", "Someone mowing", "weird"),
        p("b24", "Dog in a Car", "Dog riding in car", "weird"),
        p("b25", "Warning Sign", "A warning sign", "weird"),
        p("b26", "Rest Area Sign", "Rest Area Sign", "weird"),
        p("b27", "American Flag on Semi", "A semi showing an American Flag", "weird"),
        p("b28", "Attorney Billboard", "Sign mentioning an attorney", "weird"),
        p("b29", "24 Hour Diner", "A restaurant open 24 hours a day", "weird"),
        p("b30", "Giant Statue", "A giant roadside statue", "weird"),
        p("b31", "Police Car", "A police car", "weird"),
        p("b32", "Band Sticker", "Sticker for any band or musician", "weird"),
        p("b33", "Duct Tape Repair Job", "Car bumper or window held entirely by duct tape", "weird"),
        p("b34", "Green Car", "A green car", "weird"),
        p("b35", "Road Kill", "A poor dead animal beside the road", "weird"),
        p("b36", "Smoking Road Worker", "A street repairman smoking", "weird"),
        p("b37", "Train", "A train", "weird"),
        p("b38", "Casino Sign", "A sign for a casino", "weird"),
        p("b39", "Mexican Food Restaurant", "A mexican food restaurant", "weird"),
        p("b40", "Car Crash", "A car crash", "weird"),
        p("b41", "Expired Car Plate", "An expired license plate", "weird"),
        p("b42", "Cowboy Hat", "A cowboy hat", "weird")
    )

    // -------------------------------------------------------------
    // DECK C: MOUNTAIN WEST & ROCKIES (42 CONCISE PROMPTS)
    // -------------------------------------------------------------
    val DECK_C_PROMPTS = listOf(
        p("c1", "Ski Rack on Car", "Vehicle equipped with roof ski or snowboard carriers", "mountain", false, Region.MOUNTAIN_WEST),
        p("c2", "Snow-Capped Peak", "A mountain summit visible with snow cover", "mountain", false, Region.MOUNTAIN_WEST),
        p("c3", "Wildlife Crossing Sign", "Sign depicting elk, bighorn sheep, or deer", "signs", false, Region.MOUNTAIN_WEST),
        p("c4", "Elk or Deer in Distance", "Large game animals visible in a valley or forest edge", "nature", false, Region.MOUNTAIN_WEST),
        p("c5", "Mountain Pass Sign", "Elevation marker sign for a high pass", "signs", false, Region.MOUNTAIN_WEST),
        p("c6", "Trailhead Parking Sign", "Designated hiking trailhead sign", "signs", false, Region.MOUNTAIN_WEST),
        p("c7", "River Rafting Billboard", "Advert for whitewater rafting or kayak rentals", "outdoor", false, Region.MOUNTAIN_WEST),
        p("c8", "National Forest Boundary", "Green Smokey Bear or US Forest Service sign", "signs", false, Region.MOUNTAIN_WEST),
        p("c9", "Rock Formation Cliffs", "Sheer canyon walls or red rock formations", "landscape", false, Region.MOUNTAIN_WEST),
        p("c10", "Evergreen Forest", "Dense stands of pine, fir, or spruce trees", "nature", false, Region.MOUNTAIN_WEST),
        p("c11", "Dust-Covered 4x4", "Off-road truck or Jeep with thick dirt/mud", "vehicles", false, Region.MOUNTAIN_WEST),
        p("c12", "Overland Camper Van", "Custom Sprinter or 4x4 camper rig", "vehicles", false, Region.MOUNTAIN_WEST),
        p("c13", "Fly Fishing Access", "Public river access sign for anglers", "signs", false, Region.MOUNTAIN_WEST),
        p("c14", "Wildfire Danger Sign", "Smokey Bear meter showing fire risk level", "signs", false, Region.MOUNTAIN_WEST),
        p("c15", "Winding Canyon Road", "Road with multiple hairpin turns through rocks", "landscape", false, Region.MOUNTAIN_WEST),
        p("c16", "Mountain Lake / Reservoir", "High altitude body of water along road", "landscape", false, Region.MOUNTAIN_WEST),
        p("c17", "Ranch Gate Structure", "Large timber or iron entrance arch over driveway", "structures", false, Region.MOUNTAIN_WEST),
        p("c18", "Cattle Guard in Road", "Steel grate set in road to keep livestock in", "structures", false, Region.MOUNTAIN_WEST),
        p("c19", "Log Cabin Lodge", "Resort or house made of natural wood logs", "structures", false, Region.MOUNTAIN_WEST),
        p("c20", "Snowplow Parked or Active", "Large highway snow removal vehicle", "equipment", false, Region.MOUNTAIN_WEST),
        p("c21", "Ski Resort Chairlift", "Chairlift cables or towers visible from valley", "structures", false, Region.MOUNTAIN_WEST),
        p("c22", "Continental Divide Sign", "High point marker sign for the Divide", "signs", false, Region.MOUNTAIN_WEST),
        p("c23", "Moose Crossing Warning", "Yellow warning sign depicting a moose", "signs", false, Region.MOUNTAIN_WEST),
        p("c24", "Timber Hauling Truck", "Flatbed semi loaded with massive tree logs", "vehicles", false, Region.MOUNTAIN_WEST),
        p("c25", "Bouldering Crash Pad", "Climber vehicle with thick foam pads on roof", "vehicles", false, Region.MOUNTAIN_WEST),
        p("c26", "Runaway Truck Ramp", "Uphill gravel escape ramp on steep descent", "structures", false, Region.MOUNTAIN_WEST),
        p("c27", "Hot Springs Advert", "Billboard for natural thermal mineral baths", "signs", false, Region.MOUNTAIN_WEST),
        p("c28", "Avalanche Chute Sign", "Warning sign for avalanche danger zone", "signs", false, Region.MOUNTAIN_WEST),
        p("c29", "Ghost Town Landmark", "Historic mining town or abandoned wooden structures", "structures", false, Region.MOUNTAIN_WEST),
        p("c30", "Mountain Bike Trailhead", "Cyclist parking or singletrack trail marker", "signs", false, Region.MOUNTAIN_WEST),
        p("c31", "High Altitude Prairie", "Wide open mountain park or valley grassland", "landscape", false, Region.MOUNTAIN_WEST),
        p("c32", "Park Ranger Truck", "Green or tan National Park / Forest Service truck", "vehicles", false, Region.MOUNTAIN_WEST),
        p("c33", "Geology Information Board", "Roadside turnout display explaining rock layers", "signs", false, Region.MOUNTAIN_WEST),
        p("c34", "Solar Peak Weather Station", "Mountain ridge weather monitoring equipment", "structures", false, Region.MOUNTAIN_WEST),
        p("c35", "Frozen Waterfall", "Ice cascade hanging on canyon cliff face", "landscape", false, Region.MOUNTAIN_WEST),
        p("c36", "Gold Panning Sign", "Tourist attraction sign for gold panning", "signs", false, Region.MOUNTAIN_WEST),
        p("c37", "Fly Shop Trout Statue", "Tackle shop with a large fish or trout statue", "structures", false, Region.MOUNTAIN_WEST),
        p("c38", "Hay Bale Stacks in Canyon", "Large round bales stacked in high valley field", "nature", false, Region.MOUNTAIN_WEST),
        p("c39", "Rodeo Arena Grounds", "Corrals and grandstands for mountain rodeo", "structures", false, Region.MOUNTAIN_WEST),
        p("c40", "Wild Mustang Crossing", "Sign or herd of wild horses along highway", "nature", false, Region.MOUNTAIN_WEST),
        p("c41", "Windy Mountain Summit Flag", "Flagpole atop a windy high elevation pass", "signs", false, Region.MOUNTAIN_WEST),
        p("c42", "Mountain Solar Farm", "Array of solar panels on high elevation slope", "structures", false, Region.MOUNTAIN_WEST)
    )

    // -------------------------------------------------------------
    // DECK D: DESERT & SOUTHWEST (42 CONCISE PROMPTS)
    // -------------------------------------------------------------
    val DECK_D_PROMPTS = listOf(
        p("d1", "Saguaro Cactus", "Tall iconic multi-arm desert cactus", "desert", false, Region.SOUTHWEST),
        p("d2", "Red Rock Mesa", "Flat-topped red sandstone mountain structure", "landscape", false, Region.SOUTHWEST),
        p("d3", "Adobe Building", "Traditional Southwestern stucco/earthen structure", "structures", false, Region.SOUTHWEST),
        p("d4", "Dust Devil / Whirlwind", "Spinning column of desert dust in distance", "nature", false, Region.SOUTHWEST),
        p("d5", "Route 66 Landmark", "Historic shield sign or diner along Route 66", "signs", false, Region.SOUTHWEST),
        p("d6", "Canyon Viewpoint", "Lookout over a deep desert gorge or canyon", "landscape", false, Region.SOUTHWEST),
        p("d7", "Turquoise Jewelry Sign", "Trading post sign for authentic Native crafts", "signs", false, Region.SOUTHWEST),
        p("d8", "Solar Panel Array", "Wide field of solar energy collectors in desert", "structures", false, Region.SOUTHWEST),
        p("d9", "Cactus Flowers", "Blooms on prickly pear, cholla, or saguaro", "nature", false, Region.SOUTHWEST),
        p("d10", "Old West Saloon", "Western-style wooden storefront or restaurant", "structures", false, Region.SOUTHWEST),
        p("d11", "Dry Wash Crossing Sign", "Warning for flash flooding or dip in road", "signs", false, Region.SOUTHWEST),
        p("d12", "Lizard on Rock", "Small reptile sunning on roadside boulder", "nature", false, Region.SOUTHWEST),
        p("d13", "Windmill Water Pump", "Traditional farm/ranch steel windmill", "structures", false, Region.SOUTHWEST),
        p("d14", "Roadrunner crossing", "Sign or fast bird along desert highway", "nature", false, Region.SOUTHWEST),
        p("d15", "Dry Salt Flat / Lakebed", "Blinding white expanse of dry alkali lake", "landscape", false, Region.SOUTHWEST),
        p("d16", "Sun Bleached Skull", "Steer skull decoration on ranch fence or gate", "structures", false, Region.SOUTHWEST),
        p("d17", "Trading Post", "Historic desert stop selling souvenirs and food", "structures", false, Region.SOUTHWEST),
        p("d18", "Tumbleweed", "Dried plant blowing across or beside road", "nature", false, Region.SOUTHWEST),
        p("d19", "Desert Resort Sign", "Sprawling palm oasis or golf course sign", "signs", false, Region.SOUTHWEST),
        p("d20", "Ghost Town Ruins", "Abandoned adobe or stone pioneer buildings", "structures", false, Region.SOUTHWEST),
        p("d21", "Border Patrol Station", "Highway inspection checkpoint or vehicle", "structures", false, Region.SOUTHWEST),
        p("d22", "Chile Pepper Wreath", "Ristra of dried red chiles on porch or store", "structures", false, Region.SOUTHWEST),
        p("d23", "Pueblo Architecture", "Terraced multi-story adobe dwelling style", "structures", false, Region.SOUTHWEST),
        p("d24", "Yucca / Joshua Tree", "Spiky evergreen desert tree or plant", "nature", false, Region.SOUTHWEST),
        p("d25", "Open Range Cattle Sign", "Warning for unfenced livestock in road", "signs", false, Region.SOUTHWEST),
        p("d26", "Dust-Covered Off-Road Buggy", "Dune buggy or UTV loaded on trailer", "vehicles", false, Region.SOUTHWEST),
        p("d27", "Prickly Pear Candy Sign", "Store advertising cactus jelly or sweets", "signs", false, Region.SOUTHWEST),
        p("d28", "Desert Solar Power Tower", "Central mirror tower in solar thermal plant", "structures", false, Region.SOUTHWEST),
        p("d29", "Petroglyph Site Sign", "Marker for ancient Native American rock art", "signs", false, Region.SOUTHWEST),
        p("d30", "Dune Buggy on Trailer", "Sand dune vehicle being towed", "vehicles", false, Region.SOUTHWEST),
        p("d31", "Old Locomotive Monument", "Steam train on display in desert town park", "structures", false, Region.SOUTHWEST),
        p("d32", "Navajo / Hopi Rug Sign", "Advert for authentic handwoven textiles", "signs", false, Region.SOUTHWEST),
        p("d33", "Heat Haze Shimmer", "Mirage optical illusion on hot asphalt road", "landscape", false, Region.SOUTHWEST),
        p("d34", "Coyote Hazard Warning", "Sign or wildlife sighting along open plain", "nature", false, Region.SOUTHWEST),
        p("d35", "Desert Oasis Palm Grove", "Natural spring surrounded by wild fan palms", "landscape", false, Region.SOUTHWEST),
        p("d36", "Vintage Motel Neon Sign", "Retro Route 66 style glowing neon sign", "signs", false, Region.SOUTHWEST),
        p("d37", "Mining Haul Truck", "Giant tire or mining vehicle on display", "equipment", false, Region.SOUTHWEST),
        p("d38", "Saguaro Boot Craft", "Hollowed cactus boot decoration at store", "structures", false, Region.SOUTHWEST),
        p("d39", "Grand Canyon Billboard", "Tourist advert for flight or river tour", "signs", false, Region.SOUTHWEST),
        p("d40", "Copper Mine Overlook", "Open pit copper mine or machinery view", "landscape", false, Region.SOUTHWEST),
        p("d41", "Alien / Roswell Billboard", "Fun UFO or extraterrestrial theme advert", "signs", false, Region.SOUTHWEST),
        p("d42", "Desert Sunset Horizon", "Vibrant orange/purple evening sky landscape", "landscape", false, Region.SOUTHWEST)
    )

    val DECKS = listOf(
        Deck(
            id = "deck_a",
            title = "Classic Road Trip",
            description = "42 Classic roadside items for families & kids",
            coverIcon = "🚗",
            audience = "Families & Kids",
            difficulty = "Medium",
            isFree = true,
            region = Region.UNIVERSAL,
            prompts = DECK_A_PROMPTS
        ),
        Deck(
            id = "deck_b",
            title = "Weird Road Trip",
            description = "42 Hilarious & bizarre roadside sights for teens & adults",
            coverIcon = "🤪",
            audience = "Teens & Adults",
            difficulty = "Medium",
            isFree = true,
            region = Region.UNIVERSAL,
            prompts = DECK_B_PROMPTS
        ),
        Deck(
            id = "deck_c",
            title = "Mountain West & Rockies",
            description = "42 Mountain pass & alpine scenery sights",
            coverIcon = "🏔️",
            audience = "Everyone",
            difficulty = "Medium",
            isFree = true,
            region = Region.MOUNTAIN_WEST,
            prompts = DECK_C_PROMPTS
        ),
        Deck(
            id = "deck_d",
            title = "Desert & Southwest",
            description = "42 Cacti, canyons, and southwestern landmarks",
            coverIcon = "🌵",
            audience = "Everyone",
            difficulty = "Medium",
            isFree = true,
            region = Region.SOUTHWEST,
            prompts = DECK_D_PROMPTS
        ),
        Deck(
            id = "deck_e",
            title = "Northeast & Mid-Atlantic",
            description = "42 Coastal lighthouses, autumn forests, and bridges",
            coverIcon = "🌉",
            audience = "Everyone",
            difficulty = "Medium",
            isFree = true,
            region = Region.NORTHEAST,
            prompts = DECK_A_PROMPTS.shuffled()
        ),
        Deck(
            id = "deck_f",
            title = "The South",
            description = "42 Deep south landmarks, swamps, and country stores",
            coverIcon = "🌳",
            audience = "Everyone",
            difficulty = "Medium",
            isFree = true,
            region = Region.SOUTHEAST,
            prompts = DECK_A_PROMPTS.shuffled()
        ),
        Deck(
            id = "deck_g",
            title = "West Coast",
            description = "42 Coastal Highway 1, surf spots, and redwoods",
            coverIcon = "🏄",
            audience = "Everyone",
            difficulty = "Medium",
            isFree = true,
            region = Region.PACIFIC_SOUTHWEST,
            prompts = DECK_A_PROMPTS.shuffled()
        ),
        Deck(
            id = "deck_h",
            title = "Heart of the Country",
            audience = "Everyone",
            description = "42 Cornfields, silos, and heartland landmarks",
            coverIcon = "🌽",
            difficulty = "Medium",
            isFree = true,
            region = Region.MIDWEST,
            prompts = DECK_A_PROMPTS.shuffled()
        )
    )
}
