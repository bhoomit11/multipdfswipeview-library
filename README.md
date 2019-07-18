# multipdfswipeview-library
A simple library to show multiple PDF in swipable layout

Step 1. Add it in your root build.gradle at the end of repositories:

    allprojects {
      repositories {
      ...
      maven { url 'https://jitpack.io' }
      }
    }
    
Step 2. Add the dependency
  
    dependencies {
      implementation 'com.github.bhoomit11:multipdfswipeview-library:0.1.1'
    } 


Put this PDF view in you xml file :

    <com.example.multipdfswipeview_library.PDFMultiSwipeView
            android:id="@+id/pdfView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
            

Create config for each PDF file

for online and local PDF file

    val pdfView: PDFMultiSwipeView = findViewById(R.id.pdfView)
    
    private val pdfConfigs = ArrayList<PDFConfig>()
    
    // You can add multiple PDF like this
    var config = PDFConfig()
    config.id = "1" //PDF file ID
    config.name = "pdfSample1"
    config.pdfUrl = "https://www.antennahouse.com/XSLsample/pdf/sample-link_1.pdf"
    config.pdfLocalpath = "" //leave blank if no local path available else put absolute path of a FILE
    pdfConfigs.add(config)
    
    ...
    
    pdfView.with(this).build(pdfConfigs, PDFMultiSwipeView.TYPE_ONLINE) 
    // PDFMultiSwipeView.TYPE_OFFLINE if local path was passed in model
    
for assets PDF file

    private val pdfConfigs = ArrayList<PDFConfig>()
    
    // You can add multiple PDF like this
    var config = PDFConfig()
    config.id = "2" //PDF file ID
    config.name = "pdfSample2"
    config.pdfUrl = ""              //leave blank
    config.pdfLocalpath = ""        //leave blank
    config.assestFileName = "sample.pdf" //Pass file name of pdf file in asset folder
    pdfConfigs.add(config)
    
    ...
    
    pdfView.with(this).build(pdfConfigs, PDFMultiSwipeView.TYPE_ASSET) // TYPE_ASSET if you want to open asset file
    
Checkout sample for more info.
