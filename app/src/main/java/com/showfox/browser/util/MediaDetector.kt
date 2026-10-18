package com.showfox.browser.util

import android.util.Log
import org.mozilla.geckoview.GeckoSession

object MediaDetector {

    private const val TAG = "MediaDetector"

    private val MEDIA_PATTERNS = listOf(
        Regex("""<video[^>]+src=["']([^"']+)["']""", RegexOption.IGNORE_CASE),
        Regex("""<source[^>]+src=["']([^"']+)["'][^>]+type=["']video/[^"']+["']""", RegexOption.IGNORE_CASE),
        Regex("""<iframe[^>]+src=["']([^"']*(?:youtube|vimeo|dailymotion)[^"']*)["']""", RegexOption.IGNORE_CASE),
        Regex("""source\s*=\s*[{"']([^"']+\.(?:m3u8|mp4|webm|mpd))[^"']*["']""", RegexOption.IGNORE_CASE),
        Regex("""["']([^"']+\.m3u8[^"']*)["']""", RegexOption.IGNORE_CASE),
        Regex("""["']([^"']+\.mpd[^"']*)["']""", RegexOption.IGNORE_CASE)
    )

    var onMediaDetected: ((url: String, type: MediaType) -> Unit)? = null

    enum class MediaType {
        VIDEO, AUDIO, HLS, DASH, IFRAME_EMBED, UNKNOWN
    }

    fun injectDetectionScript(session: GeckoSession) {
        val js = """
            (function() {
                function detectMedia() {
                    var videos = document.querySelectorAll('video');
                    if (videos.length > 0) {
                        var v = videos[0];
                        var src = v.currentSrc || v.src;
                        if (src) {
                            window.GeckoView.streamDetected(src, 'video');
                        }
                    }
                    
                    var sources = document.querySelectorAll('source[type^="video"], source[type^="application"]');
                    sources.forEach(function(s) {
                        if (s.src && (s.src.includes('.m3u8') || s.src.includes('.mpd'))) {
                            window.GeckoView.streamDetected(s.src, 
                                s.src.includes('.m3u8') ? 'hls' : 'dash');
                        }
                    });
                    
                    var iframes = document.querySelectorAll('iframe[src*="youtube"], iframe[src*="vimeo"]');
                    iframes.forEach(function(f) {
                        window.GeckoView.streamDetected(f.src, 'embed');
                    });
                }
                
                var observer = new MutationObserver(function(mutations) {
                    mutations.forEach(function(m) {
                        if (m.addedNodes.length > 0) {
                            detectMedia();
                        }
                    });
                });
                
                observer.observe(document.body, { childList: true, subtree: true });
                
                if (document.readyState === 'complete') {
                    detectMedia();
                } else {
                    window.addEventListener('load', detectMedia);
                }
                
                setInterval(detectMedia, 3000);
            })();
        """.trimIndent()

        session.loadUri("javascript:$js")
    }

    fun detectFromHtml(html: String): List<Pair<String, MediaType>> {
        val results = mutableListOf<Pair<String, MediaType>>()

        MEDIA_PATTERNS.forEach { pattern ->
            pattern.findAll(html).forEach { match ->
                val url = match.groupValues[1]
                val type = when {
                    url.contains(".m3u8") -> MediaType.HLS
                    url.contains(".mpd") -> MediaType.DASH
                    url.contains("youtube") || url.contains("vimeo") -> MediaType.IFRAME_EMBED
                    url.endsWith(".mp4") || url.endsWith(".webm") -> MediaType.VIDEO
                    else -> MediaType.UNKNOWN
                }
                results.add(url to type)
                Log.d(TAG, "Detected media: $type -> $url")
            }
        }

        return results
    }
}
