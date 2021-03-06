<b>Pre-requisites:</b>
- sphinxbase & pocketsphinx (cmusphinx/pocketsphinx forked on my repository)
- acoustic model, language model, phonetic dictionary

<b>Instructions:</b>
- Install sphinxbase and pocketsphinx. For windows, I recommend using Visual Studio to build libraries. Make sure to place sphinxbase.dll and pocketsphinx.dll in your native library folder.
- Place acoustic model (en-us), language model (ngrams.lm) and phonetic dictionary (words.dic) in the pocketsphinx folder. The language model and the phonetic dictionary used in my program belong to the company, so I cannot provide them here. Generic version of the models are available to downlaod at <a href="https://cmusphinx.github.io/wiki/download/">CMU Downloads</a> and we can also refer to <a href="https://cmusphinx.github.io/wiki/tutorial/">CMU Tutorials</a> for building models and dictionaries.
- Place index.html, rtc.js, and recorder.js in the pocketsphinx folder. recorder.js. The recorder.js is library from <a href="https://github.com/mattdiamond">@mattdiamond</a>, and I added the interpolateArray function to convert the mic input's sampling rate to 16000Hz. This allows more accurate results in sphinx's hypotheses.
- Place the /test folder in pocketsphinx/swig/java.
- Import Jetty Server libraries from the /lib folder. Full version is available here: <a href="http://www.eclipse.org/jetty/download.html">Java Jetty Server</a>.
- Run ServerController.java.
- Open index.html, allow microphone.
- Click record and start reading Where the Wild Things Are.
