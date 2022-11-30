type Size = u128;

pub fn hash(input: &String) -> Size {
    debug_assert!(input.chars().all(|c| c.is_ascii_lowercase()));
    input
        .chars()
        .fold(1, |accum: Size, c: char| accum * alpha_to_prime(&c))
}

const PRIMES: [Size; 26] = [
    2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97,
    101,
];

const ALPHABET: [char; 26] = [
    'e', 't', 'a', 'o', 'i', 'n', 's', 'r', 'h', 'd', 'l', 'u', 'c', 'm', 'f', 'y', 'w', 'g', 'p',
    'b', 'v', 'k', 'x', 'q', 'j', 'z',
];

fn alpha_to_prime(c: &char) -> Size {
    debug_assert!(c.is_ascii_lowercase());
    let (idx, _) = ALPHABET
        .into_iter()
        .enumerate()
        .find(|(_, other)| other == c)
        .unwrap();
    PRIMES[idx]
}
