(ns giant-bomb.views.icon)

(defn icon
  "Renders a Font Awesome icon with the given ID
  See http://fortawesome.github.io/Font-Awesome/icons/ for all possible icons"
  [id]
  [:i {:class (str "fas fa-" (name id))}])
