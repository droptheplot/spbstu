module Main where

import System.IO
import Text.Printf (printf)
import Text.Read (readMaybe)

num = 61.0

main :: IO ()
main = do
  putStrLn "Enter start:"
  s <- getLine
  putStrLn "Enter end:"
  e <- getLine
  putStrLn "Enter delta:"
  d <- getLine
  let xs = readRange s e d
  case xs of
    Right xs ->
      let vals = map toString . map minCosCot $ xs
          lmax = maxLength $ map show xs
          rmax = maxLength vals
          lcol = map (padRight lmax) (map (printf "%.f") xs)
          rcol = map (padRight rmax) vals
       in mapM_ (\(l, r) -> printf "%s | %s\n" l r) $ (padRight lmax "X", padRight rmax "Result") : (zip lcol rcol)
    Left err -> putStrLn err

minCosCot :: Double -> Double
minCosCot x =
  let l = flip (/) num . cot $ x
      r = log . (-) 1.0 . (/) num . cos $ x
   in min l r

cot :: Floating a => a -> a
cot a = 1.0 / tan a

readRange :: String -> String -> String -> Either String [Double]
readRange s e d =
  let s' = readMaybe s :: Maybe Double
      e' = readMaybe e :: Maybe Double
      d' = readMaybe d :: Maybe Double
   in case (s',e',d') of
        (Just s', Just e', Just d') -> Right [s',(getDelta s' d')..e']
        (Nothing, _, _)             -> Left "Error parsing start."
        (_, Nothing, _)             -> Left "Error parsing end."
        (_, _, Nothing)             -> Left "Error parsing delta."

getDelta :: Double -> Double -> Double
getDelta s n
  | s == n = n+n
  | otherwise = n

toString :: Double -> String
toString n
  | isNaN n = "Error"
  | otherwise = printf "%.6f" n

maxLength :: [String] -> Int
maxLength s = foldl (\acc n -> max acc (length n)) 0 s

padRight :: Int -> String -> String
padRight n s
  | length s < n = replicate (n - length s) ' ' ++ s
  | otherwise = s
