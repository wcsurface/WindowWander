package app.vercel.windowwander.data

import app.vercel.windowwander.model.Deck
import app.vercel.windowwander.model.Prompt
import app.vercel.windowwander.model.Region

object DeckRepository {

    private fun p(
        id: String,
        text: String,
        hint: String,
        category: String = "general",
        isAudio: Boolean = false,
        region: Region = Region.UNIVERSAL
    ): Prompt = Prompt(id, text, hint, category, emptyList(), region == Region.UNIVERSAL, region)

    // -------------------------------------------------------------
    // DECK A: CLASSIC ROAD TRIP (42 CONCISE PROMPTS)
    // -------------------------------------------------------------
    val DECK_A_PROMPTS = listOf(
        p("a1", "Red Car", "Any red passenger car, truck, or SUV", "vehicles"),
        p("a2", "Motorcycle", "Any two- or three-wheeled motorcycle", "vehicles"),
        p("a3", "Semi-Truck / 18-Wheeler", "A large commercial freight truck", "vehicles"),
        p("a4", "School Bus", "A yellow school bus, active or parked", "vehicles"),
        p("a5", "Emergency Vehicle", "Police car, ambulance, or fire engine", "vehicles"),
        p("a6", "Out-of-State License Plate", "Plate from a different state than yours", "signs"),
        p("a7", "Roadside Billboard", "A large advertising sign along the highway", "signs"),
        p("a8", "Gas Station", "A fueling or service station", "structures"),
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
    // DECK B: WEIRD ROAD TRIP (42 CONCISE PROMPTS)
    // -------------------------------------------------------------
    val DECK_B_PROMPTS = listOf(
        p("b1", "Bumper Sticker Nonsense", "A sticker with an utterly baffling message", "weird"),
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
        p("b13", "Dashboard Toy Army", "Dashboard filled with bobbleheads or rubber ducks", "weird"),
        p("b14", "Hyper-Specific Shop", "A business selling ONLY left-handed scissors or rocks", "weird"),
        p("b15", "Unexpected Towing Item", "Vehicle towing a lawnmower, recliner, or hot tub", "weird"),
        p("b16", "Sign with Spelling Error", "A commercial sign with a obvious typo", "weird"),
        p("b17", "Animal Dressed as Human", "Billboard showing a dog or pig in a suit", "weird"),
        p("b18", "Comically Large Accessory", "Exhaust pipes larger than a trash can", "weird"),
        p("b19", "Unusual Color Building", "A bright pink, neon green, or purple store", "weird"),
        p("b20", "Double-Meaning Sign", "A road sign or advert open to funny interpretation", "weird"),
        p("b21", "Googly Eyes on Vehicle", "Car headlights or mirrors modified with giant eyes", "weird"),
        p("b22", "Wooden Spoiler", "A homemade wooden rear spoiler on a sedan", "weird"),
        p("b23", "Mutant Lawnmower", "A riding lawnmower driving near the highway", "weird"),
        p("b24", "Giant Peanut or Fruit", "A giant fiberglass peanut or strawberry statue", "weird"),
        p("b25", "Zombie Warning Sign", "A prank sign or advert mentioning zombies", "weird"),
        p("b26", "Tire Swing Sign", "A tire swing hanging from a roadside tree", "weird"),
        p("b27", "Pink Flamingo Yard", "A yard packed with dozens of pink lawn flamingos", "weird"),
        p("b28", "UFO Sighting Billboard", "Sign mentioning alien encounters or UFO museum", "weird"),
        p("b29", "Alien Themed Diner", "A restaurant shaped like a flying saucer", "weird"),
        p("b30", "Giant Chicken Statue", "A giant fiberglass rooster or chicken outside a store", "weird"),
        p("b31", "Skeleton Passenger", "A halloween skeleton sitting in the passenger seat", "weird"),
        p("b32", "Custom Coffin Trailer", "Trailer shaped like a coffin or casket", "weird"),
        p("b33", "Duct Tape Repair Job", "Car bumper or window held entirely by duct tape", "weird"),
        p("b34", "Giant Rocking Chair", "An oversized rocking chair outside a country store", "weird"),
        p("b35", "Chrome Wrapped Car", "A car so shiny it acts like a mirror", "weird"),
        p("b36", "Homemade Camper", "A wooden shed built on top of a pickup bed", "weird"),
        p("b37", "Taxidermy Shop Sign", "Sign featuring stuffed bear or deer heads", "weird"),
        p("b38", "Clown or Circus Sign", "Billboard or sign advertising a traveling circus", "weird"),
        p("b39", "Mystery Box Store", "A liquidation or unclaimed mail store sign", "weird"),
        p("b40", "Fake Police Cutout", "A cardboard police officer or car placed to slow traffic", "weird"),
        p("b41", "Car Covered in Turf", "A car wrapped in artificial green grass", "weird"),
        p("b42", "Giant Cowboy Boot", "A giant boot statue or building shape", "weird")
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
        p("c9", "Rock Formation Cliffs", "Sheer mountain canyon walls or red rock ridges", "landscape", false, Region.MOUNTAIN_WEST),
        p("c10", "Evergreen Forest", "Dense pine, fir, or spruce forest along highway", "landscape", false, Region.MOUNTAIN_WEST),
        p("c11", "Dust-Covered 4x4", "A Jeep or truck thick with trail dust", "vehicles", false, Region.MOUNTAIN_WEST),
        p("c12", "Overland Camper Van", "Sprinter or custom 4x4 camper van", "vehicles", false, Region.MOUNTAIN_WEST),
        p("c13", "Fly Fishing Access", "Public river fishing access point sign", "outdoor", false, Region.MOUNTAIN_WEST),
        p("c14", "Wildfire Danger Sign", "Smokey Bear sign showing fire risk level", "signs", false, Region.MOUNTAIN_WEST),
        p("c15", "Winding Canyon Road", "A series of sharp switchbacks through a canyon", "landscape", false, Region.MOUNTAIN_WEST),
        p("c16", "Mountain Lake / Reservoir", "A clear alpine lake visible from the road", "landscape", false, Region.MOUNTAIN_WEST),
        p("c17", "Ranch Gate Structure", "Large timber or iron archway gate over driveway", "structures", false, Region.MOUNTAIN_WEST),
        p("c18", "Cattle Guard in Road", "Metal grid across road replacing a fence gate", "structures", false, Region.MOUNTAIN_WEST),
        p("c19", "Log Cabin Lodge", "Log construction building or resort", "structures", false, Region.MOUNTAIN_WEST),
        p("c20", "Snowplow Parked or Active", "Highway department heavy snowplow truck", "equipment", false, Region.MOUNTAIN_WEST),
        p("c21", "Ski Resort Chairlift", "Gondola or chairlift towers visible in distance", "structures", false, Region.MOUNTAIN_WEST),
        p("c22", "Continental Divide Sign", "Sign marking the crest of the divide", "signs", false, Region.MOUNTAIN_WEST),
        p("c23", "Moose Crossing Warning", "A highway warning sign specifically for moose", "signs", false, Region.MOUNTAIN_WEST),
        p("c24", "Timber Hauling Truck", "Semi-truck carrying massive freshly cut logs", "vehicles", false, Region.MOUNTAIN_WEST),
        p("c25", "Bouldering Crash Pad", "Car with thick climbing foam pads stacked on top", "vehicles", false, Region.MOUNTAIN_WEST),
        p("c26", "Runaway Truck Ramp", "Emergency gravel ramp on steep mountain descent", "structures", false, Region.MOUNTAIN_WEST),
        p("c27", "Hot Springs Advert", "Sign for natural geothermal hot springs bath", "signs", false, Region.MOUNTAIN_WEST),
        p("c28", "Avalanche Chute Sign", "Highway sign warning of avalanche area", "signs", false, Region.MOUNTAIN_WEST),
        p("c29", "Ghost Town Landmark", "Sign or ruins of a 1800s mining town", "history", false, Region.MOUNTAIN_WEST),
        p("c30", "Mountain Bike Trailhead", "Cyclists or signs for singletrack trails", "outdoor", false, Region.MOUNTAIN_WEST),
        p("c31", "High Altitude Prairie", "Open valley floor surrounded by peaks", "landscape", false, Region.MOUNTAIN_WEST),
        p("c32", "Park Ranger Truck", "National Park Service green/white truck", "vehicles", false, Region.MOUNTAIN_WEST),
        p("c33", "Geology Information Board", "Historical display explaining rock layers", "signs", false, Region.MOUNTAIN_WEST),
        p("c34", "Solar Peak Weather Station", "Mountaintop weather radar or radio tower", "structures", false, Region.MOUNTAIN_WEST),
        p("c35", "Frozen Waterfall", "Ice cascade hanging on a rock wall in winter/spring", "landscape", false, Region.MOUNTAIN_WEST),
        p("c36", "Gold Panning Tour Sign", "Sign offering gold sluicing or mining tours", "fun", false, Region.MOUNTAIN_WEST),
        p("c37", "Fly Shop Fly Statue", "A shop with a giant trout or fishing fly sign", "structures", false, Region.MOUNTAIN_WEST),
        p("c38", "Hay Bale Stacks in Canyon", "Stacked round bales against a rocky mesa", "farm", false, Region.MOUNTAIN_WEST),
        p("c39", "Rodeo Arena Grounds", "Rodeo fence and grandstands near town", "structures", false, Region.MOUNTAIN_WEST),
        p("c40", "Wild Mustang Crossing", "Warning sign for wild horses on highway", "signs", false, Region.MOUNTAIN_WEST),
        p("c41", "Windy Mountain Summit Flag", "Flag fluttering on top of a mountain pass", "landscape", false, Region.MOUNTAIN_WEST),
        p("c42", "High Altitude Solar Farm", "Panels tilted sharply toward mountain sun", "structures", false, Region.MOUNTAIN_WEST)
    )

    // -------------------------------------------------------------
    // DECK D: DESERT & SOUTHWEST (42 CONCISE PROMPTS)
    // -------------------------------------------------------------
    val DECK_D_PROMPTS = listOf(
        p("d1", "Saguaro Cactus", "Tall branching desert cactus", "desert", false, Region.SOUTHWEST),
        p("d2", "Red Rock Mesa", "Flat-topped red rock formation", "landscape", false, Region.SOUTHWEST),
        p("d3", "Solar Energy Farm", "Dozens of solar panels lining the desert floor", "structures", false, Region.SOUTHWEST),
        p("d4", "Desert Wildlife Sign", "Warning sign for rattlesnakes, tortoises, or coyotes", "signs", false, Region.SOUTHWEST),
        p("d5", "Roadside Trading Post", "Native American crafts or souvenir shop", "structures", false, Region.SOUTHWEST),
        p("d6", "Western Saloon Sign", "Storefront or restaurant styled like a 1800s saloon", "structures", false, Region.SOUTHWEST),
        p("d7", "Dust Storm Warning", "Electronic sign warning of blowing dust / zero visibility", "signs", false, Region.SOUTHWEST),
        p("d8", "Fresh Hatch Chile Stand", "Roasting chiles emitting fragrant smoke", "food", false, Region.SOUTHWEST),
        p("d9", "Adobe Architecture", "Earth-colored stucco building with vigas", "structures", false, Region.SOUTHWEST),
        p("d10", "Dry Arroyo / Wash", "Gravel riverbed with zero water flowing", "landscape", false, Region.SOUTHWEST),
        p("d11", "Tumbleweed", "Dried bush rolling across or along the road", "nature", false, Region.SOUTHWEST),
        p("d12", "Yucca or Agave Plant", "Spiky desert plant with tall flower stalk", "nature", false, Region.SOUTHWEST),
        p("d13", "Route 66 Highway Shield", "Iconic Route 66 pavement marking or sign", "signs", false, Region.SOUTHWEST),
        p("d14", "Turquoise Building", "Building painted distinctive bright turquoise", "structures", false, Region.SOUTHWEST),
        p("d15", "Cowboy Statue", "Large statue of a cowboy or horse rider", "structures", false, Region.SOUTHWEST),
        p("d16", "Windmill Water Pump", "Traditional steel windmill pumping well water", "farm", false, Region.SOUTHWEST),
        p("d17", "Petroglyph Site Marker", "Sign pointing to ancient rock art sites", "history", false, Region.SOUTHWEST),
        p("d18", "Open Desert Straightaway", "Highway stretching straight to the horizon", "landscape", false, Region.SOUTHWEST),
        p("d19", "Cattle Skull Decoration", "Bulls skull mounted over gate or shop sign", "decor", false, Region.SOUTHWEST),
        p("d20", "Desert Botanical Garden", "Sign for cactus garden or native flora preserve", "outdoor", false, Region.SOUTHWEST),
        p("d21", "Rattlesnake Warning Sign", "Sign cautioning visitors to stay on paved paths", "signs", false, Region.SOUTHWEST),
        p("d22", "Bighorn Sheep Crossing", "Warning sign for desert bighorn sheep", "signs", false, Region.SOUTHWEST),
        p("d23", "Mining Dump Truck", "Enormous yellow earthmover at a copper mine", "equipment", false, Region.SOUTHWEST),
        p("d24", "Salt Flat Vista", "Blinding white dry lake bed visible in valley", "landscape", false, Region.SOUTHWEST),
        p("d25", "Desert Oasis Palm Cluster", "Natural spring surrounded by wild fan palms", "nature", false, Region.SOUTHWEST),
        p("d26", "Horse Trailer", "Trailer hauling horses or livestock", "vehicles", false, Region.SOUTHWEST),
        p("d27", "Roadside Jerky Stand", "Billboard or shack selling alien or beef jerky", "food", false, Region.SOUTHWEST),
        p("d28", "Hot Spring Resort Sign", "Advert for mineral hot springs soak", "signs", false, Region.SOUTHWEST),
        p("d29", "Kokopelli Flute Icon", "Traditional humpbacked flute player symbol on sign", "decor", false, Region.SOUTHWEST),
        p("d30", "Cactus Nursery", "Commercial farm selling thousands of potted cacti", "structures", false, Region.SOUTHWEST),
        p("d31", "Railroad Train in Desert", "Long freight train cutting through desert basin", "vehicles", false, Region.SOUTHWEST),
        p("d32", "Red Clay Pottery Shack", "Outdoor display of terracotta pots and chimineas", "structures", false, Region.SOUTHWEST),
        p("d33", "High Temperature Display", "Digital sign showing temperature over 100°F (or 38°C)", "signs", false, Region.SOUTHWEST),
        p("d34", "Canyon Railroad Bridge", "Steel trestle bridge spanning a dry canyon", "structures", false, Region.SOUTHWEST),
        p("d35", "Dune Buggy on Trailer", "Off-road sand rail or UTV heading to dunes", "vehicles", false, Region.SOUTHWEST),
        p("d36", "Prickly Pear Jelly Sign", "Store selling cactus jam, candy, or syrup", "food", false, Region.SOUTHWEST),
        p("d37", "Dust Devil Whirlwind", "Small funnel of spinning dust in open field", "nature", false, Region.SOUTHWEST),
        p("d38", "Pecan Orchard in Desert", "Irrigated rows of tall green trees in dry land", "farm", false, Region.SOUTHWEST),
        p("d39", "Old West Telegraph Poles", "Wooden utility poles standing alone in open sand", "structures", false, Region.SOUTHWEST),
        p("d40", "Solar Cooker / Dish", "Parabolic mirror or solar reflector research dish", "structures", false, Region.SOUTHWEST),
        p("d41", "Border Patrol Inspection", "Highway checkpoint gantry or vehicle", "structures", false, Region.SOUTHWEST),
        p("d42", "Desert Joshua Tree", "Twisted spiky Joshua tree plant", "nature", false, Region.SOUTHWEST)
    )

    // -------------------------------------------------------------
    // ALL 8 STARTER DECKS
    // -------------------------------------------------------------
    val DECKS = listOf(
        Deck("deck_a", "Classic Road Trip", "Everyday highway sight-seeing across America", "🚗", "Families & Kids", "Medium", true, Region.UNIVERSAL, DECK_A_PROMPTS),
        Deck("deck_b", "Weird Road Trip", "Hilarious, bizarre, and unique roadside oddities", "🤪", "Teens & Adults", "Medium", true, Region.UNIVERSAL, DECK_B_PROMPTS),
        Deck("deck_c", "Mountain West & Rockies", "High elevation peaks, wildlife, canyons, and winter sports", "🏔️", "Everyone", "Medium", true, Region.MOUNTAIN_WEST, DECK_C_PROMPTS),
        Deck("deck_d", "Desert & Southwest", "Mesa country, cacti, red rocks, and Route 66 culture", "🌵", "Everyone", "Medium", true, Region.SOUTHWEST, DECK_D_PROMPTS),
        Deck("deck_e", "Northeast & Mid-Atlantic", "Covered bridges, colonial landmarks, turnpikes, and fall foliage", "🌉", "Everyone", "Medium", true, Region.NORTHEAST, DECK_A_PROMPTS),
        Deck("deck_f", "The South", "Live oaks, bayous, diner signs, and coastal plains", "🌳", "Everyone", "Medium", true, Region.SOUTHEAST, DECK_A_PROMPTS),
        Deck("deck_g", "West Coast", "Pacific coast highway, redwoods, surfboards, and fruit stands", "🏄", "Everyone", "Medium", true, Region.PACIFIC_SOUTHWEST, DECK_A_PROMPTS),
        Deck("deck_h", "Heart of the Country", "Midwest cornfields, silos, giant barns, and river towns", "🌽", "Everyone", "Medium", true, Region.MIDWEST, DECK_A_PROMPTS)
    )
}
